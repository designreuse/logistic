package com.frame.web.business.service.serviceImpl;

import com.logistics.core.http.BusinessException;
import com.logistics.core.shiro.ShiroUtil;
import com.logistics.core.sql.PageUtil;
import com.logistics.core.utils.EncryptUtil;
import com.logistics.web.dao.UserDao;
import com.logistics.web.dao.pending.UserPendingDao;
import com.logistics.web.dao.ref.RefUserRoleDao;
import com.logistics.web.entity.enumeration.Revision;
import com.logistics.web.entity.enumeration.Sync;
import com.logistics.web.entity.orgainzation.User;
import com.logistics.web.entity.pending.BasePend;
import com.logistics.web.entity.pending.UserPend;
import com.logistics.web.service.UserService;
import org.apache.logging.log4j.util.Strings;
import org.camunda.bpm.engine.IdentityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl implements UserService {

    Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserDao userDao;
    @Autowired
    private UserPendingDao userPendingDao;
    @Autowired
    private RefUserRoleDao refUserRoleDao;
    @Autowired
    private IdentityService identityService;

    @Override
    public User save(User user){
        user = validateUser(user);
        user.setRevision(Revision.ACTIVE.getInt());
        user.setSync(Sync.PENDING.getInt());
        user.setId(Strings.isEmpty(user.getId()) ? EncryptUtil.randomUUID() : user.getId());
        user.setUpdateTime(LocalDateTime.now());
        userDao.save(user);
        user = null;
        user.getAccount();
        return user;
    }

    @Override
    @Transactional
    public User saveOrUpdate(User user){
        userDao.saveOrUpdate(user);
        return user;
    }

    @Override
    @Transactional
    public User pending(User user) {
        user = validateUser(user);
        user.setRevision(Revision.PENDING.getInt());
        user.setSync(Sync.INVALID.getInt());
        user.setId(Strings.isEmpty(user.getId()) ? EncryptUtil.randomUUID() : user.getId());
        user.setUpdateTime(LocalDateTime.now());
        user = userDao.saveOrUpdate(user);
        UserPend userPending = new UserPend();
        userPending.setId(EncryptUtil.randomUUID());
        userPending.setUserId(user.getId());
        userPending.setApplier(ShiroUtil.getCurrentUser().getAccount());
        userPending.setOperation(BasePend.Operation.INSERT.toString());
        userPending.setApplyTime(LocalDateTime.now());
        userPendingDao.save(userPending);
        return user;
    }

    @Override
    @Transactional
    public void activeUser(String account, boolean resolve) {
        User user = userDao.findByAccountAndRevision(account, Revision.PENDING.getInt());
        UserPend userPending = userPendingDao.findByUserId(user.getId());
        userPending.setApprover(ShiroUtil.getCurrentUser().getAccount());
        userPending.setApproveTime(LocalDateTime.now());
        if (resolve) {
            userPending.setResult(UserPend.Result.RESOLVE.toString());
            user.setRevision(Revision.ACTIVE.getInt());
            user.setSync(Sync.PENDING.getInt());
            userDao.saveOrUpdate(user);
        } else {
            userPending.setResult(UserPend.Result.REJECT.toString());
            userDao.deleteById(user.getId());
        }
        userPendingDao.save(userPending);
    }

    @Override
    public User getUserByAccount(String account) {
        return userDao.findByAccount(account);
    }

    @Override
    @Transactional
    public void deleteUser(String userId) {
        User user = userDao.findById(userId).get();
        if ("admin".equals(user.getAccount())) {
            logger.error("删除用户失败！不能删除admin账号");
            throw new BusinessException("不能删除admin账号");
        }
        refUserRoleDao.deleteByAccount(user.getAccount());
        userDao.deleteObject(user);
        // 删除人员,并自动把人员关联信息删除
        identityService.deleteUser(user.getAccount());
    }

    @Override
    public PageUtil getUserList(String name, String mobile, String deptId, Integer revision, PageUtil page) {
        return userDao.getUserList(name, mobile, deptId, revision, page);
    }

    @Override
    public PageUtil getPendList(String applier, PageUtil page) {
        return userPendingDao.pendList(ShiroUtil.getCurrentUser().getAccount(), page);
    }

    @Override
    public User getUserById(String userId) {
        return userDao.findById(userId).get();
    }

    private User validateUser(User user) {
        if (Strings.isEmpty(user.getAccount())) {
            logger.error("用户保存失败！账号不能为空");
            throw new BusinessException("账号不能为空");
        }
        User accountUser = userDao.findByAccount(user.getAccount());
        if (accountUser == null) {
            user.setCreateTime(LocalDateTime.now());
        } else if (!accountUser.getId().equals(user.getId())) {
            logger.error("用户保存失败！账号已存在");
            throw new BusinessException("账号已存在");
        }
        if (accountUser == null && Strings.isEmpty(user.getPassword())) {
            logger.error("用户保存失败！密码不能为空");
            throw new BusinessException("密码不能为空");
        } else if (accountUser == null) {
            user.setSalt(EncryptUtil.randomUUID());
            user.setPassword(EncryptUtil.simpleHash(user.getPassword(), user.getSalt(), 2));
        }
        User target = new User();
        BeanUtils.copyProperties(user, target);
        return target;
    }
}
