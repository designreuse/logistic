package com.frame.web.business.dao.pending;

import com.frame.core.sql.Pager;
import com.frame.web.base.baseRepository.BaseRepository;
import com.frame.web.business.entity.pending.UserPend;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public interface UserPendingDao extends BaseRepository<UserPend,String> {
    UserPend findByUserId(String userId);

    default Pager pendList(String applier, Pager page){
        Map<String,Object> param = new HashMap<>();
        param.put("applier",applier);
        return findByHql(page,param);
    }
}
