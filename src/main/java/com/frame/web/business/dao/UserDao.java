package com.frame.web.business.dao;

import com.frame.core.sql.Pager;
import com.frame.web.base.baseRepository.BaseRepository;
import com.frame.web.base.login.BaseUser;
import com.frame.web.base.login.LoginUserDao;
import com.frame.web.business.entity.orgainzation.Dept;
import com.frame.web.business.entity.orgainzation.User;
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

@Repository
public interface UserDao extends BaseRepository<User, String>, LoginUserDao {

    @Override
    List<BaseUser> findAllBySync(Integer sync);

    User findByAccount(String account);

    User findByAccountAndRevision(String account, Integer revision);

    default Pager getUserList(String name, String mobile, String deptId, Integer revision, Pager page) {
        Table tUser = _t(getTableName(User.class), "u");
        Table tDept = _t(getTableName(Dept.class), "d");

        List<Predicate> clauses = new ArrayList<>();
        clauses.add(eq(_w(tUser,"revision"),revision));
        if (Strings.isNotEmpty(name)) {
            clauses.add(eq(_w(tUser, "name"), name));
        }
        if (Strings.isNotEmpty(mobile)) {
            clauses.add(eq(_w(tUser, "mobile"), mobile));
        }
        if (Strings.isNotEmpty(deptId)) {
            clauses.add(eq(_w(tUser, "dept_id"), deptId));
        }

        SelectCreator select = SQL.select()
                .column(tUser, "id", "account", "name", "sex", "age", "mobile")
//                .column(tUser, "*")
                .column(tDept, "name as deptName")
                .from(tUser)
                .leftJoin(_j(tUser, tDept).on("dept_id", "id"))
                .where(clauses)
                .orderBy(tUser, "create_time", true);
        page = findBySql(select.toString(), User.ListVo.class,page, select.getParameter());
        return page;
    }
}
