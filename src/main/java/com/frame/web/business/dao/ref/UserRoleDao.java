package com.frame.web.business.dao.ref;

import com.frame.core.sql.Record;
import com.frame.web.base.baseRepository.BaseRepository;
import com.frame.web.base.login.BaseUserRole;
import com.frame.web.base.login.LoginUserRoleDao;
import com.frame.web.business.entity.orgainzation.Role;
import com.frame.web.business.entity.orgainzation.User;
import com.frame.web.business.entity.ref.UserRole;
import com.safintel.sql.statement.Predicate;
import com.safintel.sql.statement.SQL;
import com.safintel.sql.statement.SelectCreator;
import com.safintel.sql.statement.support.Table;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static com.safintel.sql.statement.Predicates.eq;
import static com.safintel.sql.statement.support.Support.*;
import static com.safintel.sql.statement.support.Support._j;

@Repository
public interface UserRoleDao extends BaseRepository<UserRole,String>, LoginUserRoleDao {

    @Override
    List<BaseUserRole> findAllByAccount(String account);

    int deleteAllByIdIn(List<String> ids);

    int deleteByAccount(String account);

    default List<Record> roleRefList(String role, String account) {
        Table tUR = _t(getTableName(UserRole.class),"re");
        Table tRole = _t(getTableName(Role.class),"ro");
        Table tUser = _t(getTableName(User.class),"u");
        List<Predicate> params = new ArrayList<>();
        if (Strings.isNotEmpty(role)) {
            params.add(eq(_w(tRole,"role"), role));
        }
        if (Strings.isNotEmpty(account)) {
            params.add(eq(_w(tUser,"account"), account));
        }
        SelectCreator sql = SQL.select().column("re.id", "ro.name as rName", "ro.code","u.account","u.name as uName")
                .from(tUR)
                .leftJoin(_j(tUR, tRole).on("role", "code"))
                .leftJoin(_j(tUR, tUser).on("account", "account"))
                .where(params);
        return findBySqlRecord(sql.toString(), sql.getParameter());
    }
}
