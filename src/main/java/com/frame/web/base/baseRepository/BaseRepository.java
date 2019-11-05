package com.frame.web.base.baseRepository;

import com.frame.core.sql.Pager;
import com.frame.core.sql.Record;
import com.frame.core.sql.RefRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@NoRepositoryBean
public interface BaseRepository<T,ID extends Serializable> extends JpaRepository<T,ID> {
    @Transactional
    void deleteObject(T entity);

    @Transactional
    int deleteByParam(Map<String, Object> params);

    @Transactional
    int executeSql(String sql);

    T find(Object id);

    @Transactional
    T saveOrUpdate(T entity);

    Object findOne(Map<String, Object> params);

    List<T> findAll(Map<String, Object> params);

    Integer findAllCount(Map<String, Object> params);

    Pager findByPage(Pager page);

    List<T> findByHql(String hql, Map<String, Object> params);

    List<T> findByHql(String hql, Object... params);

    List<T> findBySql(String sql, Map<String, Object> params);

    List<T> findBySql(String sql, Object... params);

    List<Record> findBySqlRecord(String sql, Map<String, Object> params);

    List<Record> findBySqlRecord(String sql, Object... params);

    Pager findByHql(String hql, Pager page, Map<String, Object> params);

    Pager findByHql(String hql, Pager page, Object... params);

    Pager findByHql(Pager page, Map<String, Object> params);

    <D> Pager findBySql(String sql, Class<D> dClass, Pager page, Map<String, Object> params);

    <D> Pager findBySql(String sql, Class<D> dClass, Pager page, Object... params);

    <D> Pager findBySql(Class<D> dClass, Pager page, Map<String, Object> params);

    @Transactional
    void refRuleOperation(List<RefRule<T>> rules);

    @Transactional
    void refRuleOperation(RefRule<T> rule);

    /* 获取表名 */
    String getTableName(Class<?> clazz);

    /* 获取主键 */
    String getPkname();
}
