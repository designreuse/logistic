package com.frame.web.business.dao;

import com.frame.core.sql.Pager;
import com.frame.web.base.baseRepository.BaseRepository;
import com.frame.web.base.login.LoginRoleDao;
import com.frame.web.business.entity.orgainzation.Role;
import com.safintel.sql.statement.Predicate;
import com.safintel.sql.statement.SQL;
import com.safintel.sql.statement.SelectCreator;
import com.safintel.sql.statement.support.Table;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static com.safintel.sql.statement.Predicates.eq;
import static com.safintel.sql.statement.Predicates.like;
import static com.safintel.sql.statement.support.Support._t;
import static com.safintel.sql.statement.support.Support._w;

@Repository
public interface RoleDao extends BaseRepository<Role,String>, LoginRoleDao {

    default Pager roleList(String name, String code, Pager page){
        Table role = _t(getTableName(Role.class));
        List<Predicate> clauses = new ArrayList<>();
        if (Strings.isNotEmpty(name)) {
            clauses.add(like(_w(role,"name"),"%"+name+"%"));
        }
        if (Strings.isNotEmpty(code)) {
            clauses.add(eq(_w(role,"code"), code));
        }
        SelectCreator select = SQL.select()
                .column("*")
                .from(role)
                .where(clauses)
                .orderBy(role,"create_time", true);
        page = findBySql(select.toString(),Role.class, page, select.getParameter());
        return page;
    }
}
