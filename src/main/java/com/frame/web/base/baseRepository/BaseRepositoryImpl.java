package com.frame.web.base.baseRepository;

import com.frame.core.sql.*;
import com.frame.web.base.baseRepository.builder.HqlBuilder;
import com.frame.web.base.baseRepository.builder.SqlBuilder;
import org.hibernate.query.internal.NativeQueryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.Query;
import javax.persistence.Table;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class BaseRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements BaseRepository<T, ID> {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final EntityManager entityManager;

    /* 实体类 */
    private Class<T> entityClass;
    /* 表名 */
    private String tablename;
    /* 主键名称 */
    private String pkname;

    public BaseRepositoryImpl(Class<T> domainClass, EntityManager entityManager) {
        super(domainClass, entityManager);
        this.entityManager = entityManager;
        this.entityClass = getDomainClass();
        this.tablename = getTableName(this.entityClass);
        this.pkname = getPkname();
    }

    public BaseRepositoryImpl(JpaEntityInformation<T, Serializable> information, EntityManager entityManager) {
        super(information, entityManager);
        this.entityManager = entityManager;
    }

    /**
     * 删除 by Object
     */
    @Override
    @Transactional
    public void deleteObject(T entity) {
        Assert.notNull(entity, "该实体不能为null！");
        entityManager.remove(entityManager.contains(entity) ? entity : entityManager.merge(entity));
    }

    /**
     * 根据字段删除
     *
     * @param params
     * @return
     */
    @Override
    @Transactional
    public int deleteByParam(Map<String, Object> params) {
        Assert.notNull(params, "参数不能为空");
        StringBuilder sqlBuilder = new StringBuilder("DELETE FROM ").append(this.tablename);
        List<String> wheres = params.keySet().stream().map(key -> new StringBuilder(key).append(" =:").append(key).toString())
                .collect(Collectors.toList());
        appendList(sqlBuilder, wheres, " where ", " and ");
        Query query = entityManager.createNativeQuery(sqlBuilder.toString());
        params.forEach((key, val) -> {
            query.setParameter(key, val);
        });
        return query.executeUpdate();
    }

    /**
     * 执行sql语句
     */
    @Override
    @Transactional
    public int executeSql(String sql) {
        Assert.notNull(sql, "参数sql不能为null！");
        logger.info("执行语句executeSql：" + sql);
        Query query = entityManager.createNativeQuery(sql);
        return query.executeUpdate();
    }

    /**
     * 查找 one by Id
     */
    @Override
    public T find(Object id) {
        Assert.notNull(id, "ID不能为空");
        return entityManager.find(entityClass, id);
    }

    @Override
    @Transactional
    public T saveOrUpdate(T entity) {
        Assert.notNull(entity, "对象不能为空");
        try {
            String sql;
            if (!existsById((ID) (getPkValue(entity)))) {
                entityManager.persist(entity);
            } else {
                Map<String, Object> params = new HashMap<>();
                sql = updateBuilder(entity, params);
                Query query = addParams(entityManager.createQuery(sql), params);
                query.executeUpdate();
            }
            entityManager.close();
            return entity;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }


    /*>>>>>>>>>>>>>>>>>>>>>>>>>>> HQL - START >>>>>>>>>>>>>>>>>>>>>>*/

    /**
     * 查找 one by params
     */
    @Override
    public Object findOne(Map<String, Object> params) {
        try {
            Assert.notNull(params, "params:params不能为null！");
            HqlBuilder hqlBuilder = new HqlBuilder(this.entityClass.getSimpleName(), "t");
            params.entrySet().forEach(entry -> {
                hqlBuilder.where(entry.getKey() + "= :" + entry.getKey());
            });
            logger.info("执行语句findOne：" + hqlBuilder);
            Query query = entityManager.createQuery(hqlBuilder.toString());
            query = addParams(query, params);
            Object entity = query.getSingleResult();
            entityManager.close();
            return entity;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    /**
     * 查找 all by params
     *
     * @param params
     * @return
     */
    @Override
    public List<T> findAll(Map<String, Object> params) {
        Assert.notNull(params, "params:params不能为null！");
        HqlBuilder hqlBuilder = new HqlBuilder(this.entityClass.getSimpleName(), "t");
        params.entrySet().forEach(entry -> {
            hqlBuilder.where(entry.getKey() + "= :" + entry.getKey());
        });
        logger.info("执行语句findAll：" + hqlBuilder);
        Query query = entityManager.createQuery(hqlBuilder.toString());
        query = addParams(query, params);
        List<T> list = query.getResultList();
        entityManager.close();
        return list;
    }

    /**
     * 查找 all by params
     *
     * @param params
     * @return
     */
    @Override
    public Integer findAllCount(Map<String, Object> params) {
        Assert.notNull(params, "params:params不能为null！");
        HqlBuilder hqlBuilder = new HqlBuilder(this.entityClass.getSimpleName(), "t");
        params.entrySet().forEach(entry -> {
            hqlBuilder.where(entry.getKey() + "= :" + entry.getKey());
        });
        logger.info("执行语句findAll：" + hqlBuilder);
        Query query = entityManager.createQuery(hqlBuilder.toCountString());
        query = addParams(query, params);
        Integer count = (Integer) query.getSingleResult();
        entityManager.close();
        return count;
    }

    /**
     * 查找 page by pageUtil
     */
    @Override
    public Pager findByPage(Pager page) {
        Assert.notNull(page, "PageUtil:page不能为null！");
        HqlBuilder hqlBuilder = new HqlBuilder(this.entityClass.getSimpleName(), "t")
                .orderBy(page.getSort().trim());
        logger.info("执行语句findByPage：" + hqlBuilder);
        // 数据
        Query query = entityManager.createQuery(hqlBuilder.toString());
        query.setFirstResult((page.getPageIndex() - 1) * page.getPageSize());
        query.setMaxResults(page.getPageSize());
        List<T> list = query.getResultList();
        page.setData(list);
        // 页码
        logger.info("执行语句findByPage-Count：" + hqlBuilder.toCountString());
        query = entityManager.createQuery(hqlBuilder.toCountString());
        Number number = (Number) query.getSingleResult();
        page.setTotalRowCount(number.intValue());
        page.setTotalPageCount((number.intValue() + page.getPageSize() - 1) / page.getPageSize());
        entityManager.close();
        return page;
    }

    /**
     * 查找 list by hql and map-params
     */
    @Override
    public List<T> findByHql(String hql, Map<String, Object> params) {
        logger.info("执行语句findByHql：" + hql);
        Query query = entityManager.createQuery(hql);
        query = addParams(query, params);
        List<T> list = query.getResultList();
        entityManager.close();
        return list;
    }

    /**
     * 查找 list by hql and list-params
     */
    @Override
    public List<T> findByHql(String hql, final Object... params) {
        logger.info("执行语句findByHql：" + hql);
        Query query = entityManager.createQuery(hql);
        query = addParams(query, params);
        List<T> list = query.getResultList();
        entityManager.close();
        return list;
    }

    /**
     * 查找 list by sql and map-params
     */
    @Override
    public List<T> findBySql(String sql, Map<String, Object> params) {
        logger.info("执行语句findBySql：" + sql);
        Query query = entityManager.createNativeQuery(sql);
        query = addParams(query, params);
        List<T> list = query.getResultList();
        entityManager.close();
        return list;
    }

    /**
     * 查找 list by sql and list-params
     */
    @Override
    public List<T> findBySql(String sql, final Object... params) {
        logger.info("执行语句findBySql：" + sql);
        Query query = entityManager.createNativeQuery(sql);
        query = addParams(query, params);
        List<T> list = query.getResultList();
        entityManager.close();
        return list;
    }

    /**
     * 查找 Record by sql and map-params
     */
    @Override
    public List<Record> findBySqlRecord(String sql, Map<String, Object> params) {
        logger.info("执行语句findBySqlRecord：" + sql);
        Query query = entityManager.createNativeQuery(sql);
        query = addParams(query, params);
        query.unwrap(NativeQueryImpl.class).setResultTransformer(AliasToRecordTransformer.INSTANCE);
        List<Record> list = query.getResultList();
        entityManager.close();
        return list;
    }

    /**
     * 查找 Record by hql and list-params
     */
    @Override
    public List<Record> findBySqlRecord(String sql, final Object... params) {
        logger.info("执行语句findBySqlRecord：" + sql);
        Query query = entityManager.createNativeQuery(sql);
        query = addParams(query, params);
        query.unwrap(NativeQueryImpl.class).setResultTransformer(AliasToRecordTransformer.INSTANCE);
        List<Record> list = query.getResultList();
        entityManager.close();
        return list;
    }

    /**
     * 查找 Page by hql and map-params
     */
    @Override
    public Pager findByHql(String hql, Pager page, Map<String, Object> params) {
        // 数据
        logger.info("执行语句findByHql：" + hql);
        Query query = entityManager.createQuery(hql);
        query = addParams(query, params);
        query.setFirstResult((page.getPageIndex() - 1) * page.getPageSize());
        query.setMaxResults(page.getPageSize());
        List<T> list = query.getResultList();
        page.setData(list);
        // 分页
        logger.info("执行语句findByHql-count：" + toCountString(hql));
        query = entityManager.createQuery(toCountString(hql));
        query = addParams(query, params);
        Number number = (Number) query.getSingleResult();
        page.setTotalRowCount(number.intValue());
        page.setTotalPageCount((number.intValue() + page.getPageSize() - 1) / page.getPageSize());
        entityManager.close();
        return page;
    }

    /**
     * 查找 Page by hql and list-params
     */
    @Override
    public Pager findByHql(String hql, Pager page, final Object... params) {
        // 数据
        logger.info("执行语句findByHql：" + hql);
        Query query = entityManager.createQuery(hql);
        query = addParams(query, params);
        query.setFirstResult((page.getPageIndex() - 1) * page.getPageSize());
        query.setMaxResults(page.getPageSize());
        List<T> list = query.getResultList();
        page.setData(list);
        // 分页
        logger.info("执行语句findByHql-count：" + toCountString(hql));
        query = entityManager.createQuery(toCountString(hql));
        query = addParams(query, params);
        Number number = (Number) query.getSingleResult();
        page.setTotalRowCount(number.intValue());
        page.setTotalPageCount((number.intValue() + page.getPageSize() - 1) / page.getPageSize());
        entityManager.close();
        return page;
    }

    @Override
    public Pager findByHql(Pager page, Map<String, Object> params) {
        HqlBuilder hqlBuilder = new HqlBuilder(this.entityClass.getSimpleName(), "t");
        params.entrySet().forEach(entry -> {
            hqlBuilder.where(entry.getKey() + "= :" + entry.getKey());
        });
        // 数据
        logger.info("执行语句findByHql：" + hqlBuilder);
        Query query = entityManager.createQuery(hqlBuilder.toString());
        query = addParams(query, params);
        query.setFirstResult((page.getPageIndex() - 1) * page.getPageSize());
        query.setMaxResults(page.getPageSize());
        List<T> list = query.getResultList();
        page.setData(list);
        // 分页
        logger.info("执行语句findByHql-count：" + hqlBuilder.toCountString());
        query = entityManager.createQuery(hqlBuilder.toCountString());
        query = addParams(query, params);
        Number number = (Number) query.getSingleResult();
        page.setTotalRowCount(number.intValue());
        page.setTotalPageCount((number.intValue() + page.getPageSize() - 1) / page.getPageSize());
        entityManager.close();
        return page;
    }

    /**
     * 查找 Page by sql and map-params
     *
     * @param sql    sql语句
     * @param dClass 返回的类型
     * @param page   分页
     * @param params 参数
     * @param <D>
     * @return
     */
    @Override
    public <D> Pager findBySql(String sql, Class<D> dClass, Pager page, Map<String, Object> params) {
        logger.info("执行语句findBySql：" + sql);
        Query query = entityManager.createNativeQuery(sql);
        query = addParams(query, params);
        query.setFirstResult((page.getPageIndex() - 1) * page.getPageSize());
        query.setMaxResults(page.getPageSize());
        if (dClass == Record.class) {
            query.unwrap(NativeQueryImpl.class).setResultTransformer(AliasToRecordTransformer.INSTANCE);
            List<Record> list = query.getResultList();
            page.setData(list);
        } else {
            query.unwrap(NativeQueryImpl.class).setResultTransformer(new AliasToBeanTransformer(dClass));
            List<D> list = query.getResultList();
            page.setData(list);
        }
        // 分页
        logger.info("执行语句findBySql-count：" + toCountString(sql));
        query = entityManager.createNativeQuery(toCountString(sql));
        query = addParams(query, params);
        Number number = (Number) query.getSingleResult();
        page.setTotalRowCount(number.intValue());
        page.setTotalPageCount((number.intValue() + page.getPageSize() - 1) / page.getPageSize());
        entityManager.close();
        return page;
    }

    /**
     * 查找 Page by sql and list-params
     */
    @Override
    public <D> Pager findBySql(String sql, Class<D> dClass, Pager page, final Object... params) {
        logger.info("执行语句findBySql：" + sql);
        Query query = entityManager.createNativeQuery(sql);
        query = addParams(query, params);
        query.setFirstResult((page.getPageIndex() - 1) * page.getPageSize());
        query.setMaxResults(page.getPageSize());
        if (dClass == Record.class) {
            query.unwrap(NativeQueryImpl.class).setResultTransformer(AliasToRecordTransformer.INSTANCE);
            List<Record> list = query.getResultList();
            page.setData(list);
        } else {
            query.unwrap(NativeQueryImpl.class).setResultTransformer(new AliasToBeanTransformer(dClass));
            List<D> list = query.getResultList();
            page.setData(list);
        }
        // 分页
        logger.info("执行语句findBySql-count：" + toCountString(sql));
        query = entityManager.createNativeQuery(toCountString(sql));
        query = addParams(query, params);
        Number number = (Number) query.getSingleResult();
        page.setTotalRowCount(number.intValue());
        page.setTotalPageCount((number.intValue() + page.getPageSize() - 1) / page.getPageSize());
        entityManager.close();
        return page;
    }

    @Override
    public <D> Pager findBySql(Class<D> dClass, Pager page, Map<String, Object> params) {
        SqlBuilder sqlBuilder = new SqlBuilder(this.entityClass.getSimpleName());
        params.entrySet().forEach(entry -> {
            sqlBuilder.where(entry.getKey() + "= :" + entry.getKey());
        });
        // 数据
        logger.info("执行语句findByHql：" + sqlBuilder);
        Query query = entityManager.createQuery(sqlBuilder.toString());
        query = addParams(query, params);
        query.setFirstResult((page.getPageIndex() - 1) * page.getPageSize());
        query.setMaxResults(page.getPageSize());
        if (dClass == Record.class) {
            query.unwrap(NativeQueryImpl.class).setResultTransformer(AliasToRecordTransformer.INSTANCE);
            List<Record> list = query.getResultList();
            page.setData(list);
        } else {
            query.unwrap(NativeQueryImpl.class).setResultTransformer(new AliasToBeanTransformer(dClass));
            List<D> list = query.getResultList();
            page.setData(list);
        }
        // 分页
        logger.info("执行语句findByHql-count：" + sqlBuilder.toCountString());
        query = entityManager.createQuery(sqlBuilder.toCountString());
        query = addParams(query, params);
        Number number = (Number) query.getSingleResult();
        page.setTotalRowCount(number.intValue());
        page.setTotalPageCount((number.intValue() + page.getPageSize() - 1) / page.getPageSize());
        entityManager.close();
        return page;
    }

    @Override
    @Transactional
    public void refRuleOperation(List<RefRule<T>> rules) {
        rules.forEach((rule) -> {
            switch (rule.getRule()) {
                case INSERT:
                    save(rule.getEntity());
                    break;
                case DELETE:
                    delete(rule.getEntity());
                    break;
            }
        });
    }

    @Override
    @Transactional
    public void refRuleOperation(RefRule<T> rule) {
        switch (rule.getRule()) {
            case INSERT:
                save(rule.getEntity());
                break;
            case DELETE:
                delete(rule.getEntity());
                break;
        }
    }


    /* 公用 - 添加参数值 Map */
    private Query addParams(Query query, Map<String, Object> params) {
        if (params != null) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                query.setParameter(entry.getKey(), entry.getValue());
            }
        }
        return query;
    }

    /* 公用 - 添加参数值 List */
    private Query addParams(Query query, final Object[] params) {
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                query.setParameter(i + 1, params[i]);
            }
        }
        return query;
    }

    /* 获取表名 */
    @Override
    public String getTableName(Class<?> clazz) {
        Table annotation = clazz.getAnnotation(Table.class);
        if (annotation != null) {
            return annotation.name();
        }
        return null;
    }

    /* 获取主键 */
    @Override
    public String getPkname() {
        List<Field> fieldList = getDeclaredFields(this.entityClass);
        for (Field field : fieldList) {
            if (field.isAnnotationPresent(Id.class)) {
                this.pkname = field.getName();
                break;
            }
        }
        return pkname;
    }

    /* 获取主键值*/
    public Object getPkValue(T entity) {
        try {
            List<Field> fieldList = getDeclaredFields(this.entityClass);
            for (Field field : fieldList) {
                if (field.isAnnotationPresent(Id.class)) {
                    field.setAccessible(true);
                    return field.get(entity);
                }
            }
        } catch (Exception e) {
        }
        return null;
    }

    protected void appendList(StringBuilder sql, List<?> list, String init, String sep) {
        boolean first = true;
        for (Iterator var6 = list.iterator(); var6.hasNext(); first = false) {
            Object s = var6.next();
            if (first) {
                sql.append(init);
            } else {
                sql.append(sep);
            }
            sql.append(s);
        }
    }

    private List<Field> getDeclaredFields(Class entityClass) {
        List<Field> fieldList = new ArrayList<>();
        Class nowCalss = entityClass;
        while (nowCalss!=null) {
            fieldList.addAll(Arrays.asList(nowCalss.getDeclaredFields()));
            nowCalss = nowCalss.getSuperclass();
        }
        return fieldList;
    }

    private String toCountString(String sql) {
        SqlBuilder sqlBuilder = new SqlBuilder()
                .column("COUNT(*)")
                .from(new StringBuilder("(").append(sql).append(")tmp").toString());
        return sqlBuilder.toString();
    }

    private String updateBuilder(T entity, Map<String, Object> params) throws IllegalAccessException {
        StringBuilder updateSql = new StringBuilder("UPDATE ").append(this.entityClass.getSimpleName()).append(" o SET ");
        Field[] fields = this.entityClass.getDeclaredFields();
        String pkName = null;
        Object id = null;
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(Id.class)) {
                pkName = field.getName();
                id = field.get(entity);
                params.put(field.getName(), field.get(entity));
            } else if (field.get(entity) != null) {
                updateSql.append("o.").append(field.getName()).append("=:").append(field.getName()).append(",");
                params.put(field.getName(), field.get(entity));
            }
        }
        return updateSql.replace(updateSql.length() - 1, updateSql.length(), "")
                .append(" WHERE o.").append(pkName).append("=:").append(pkName).toString();
    }
}
