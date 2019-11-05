package com.frame.web.base.baseRepository.builder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SqlBuilder implements Serializable {
    private static final long serialVersionUID = 1L;
    private boolean distinct;
    private List<Object> columns = new ArrayList();
    private List<String> tables = new ArrayList();
    private List<String> joins = new ArrayList();
    private List<String> leftJoins = new ArrayList();
    private List<String> wheres = new ArrayList();
    private List<String> groupBys = new ArrayList();
    private List<String> havings = new ArrayList();
    private List<SqlBuilder> unions = new ArrayList();
    private List<String> orderBys = new ArrayList();
    private int offset = 0;
    private int limit = 0;
    private boolean forUpdate;
    private boolean noWait;

    public SqlBuilder() {
    }

    public SqlBuilder(String table) {
        this.tables.add(table);
    }


    public SqlBuilder and(String expr) {
        return this.where(expr);
    }

    public SqlBuilder column(String name) {
        this.columns.add(name);
        return this;
    }

    public SqlBuilder column(String name, boolean groupBy) {
        this.columns.add(name);
        if (groupBy) {
            this.groupBys.add(name);
        }

        return this;
    }

    public SqlBuilder limit(int offset, int limit) {
        this.offset = offset;
        this.limit = limit;
        return this;
    }

    public SqlBuilder limit(int limit) {
        return this.limit(0, limit);
    }

    public SqlBuilder distinct() {
        this.distinct = true;
        return this;
    }

    public SqlBuilder forUpdate() {
        this.forUpdate = true;
        return this;
    }

    public SqlBuilder from(String table) {
        this.tables.add(table);
        return this;
    }

    public List<SqlBuilder> getUnions() {
        return this.unions;
    }

    public SqlBuilder groupBy(String expr) {
        this.groupBys.add(expr);
        return this;
    }

    public SqlBuilder having(String expr) {
        this.havings.add(expr);
        return this;
    }

    public SqlBuilder join(String join) {
        this.joins.add(join);
        return this;
    }

    public SqlBuilder leftJoin(String join) {
        this.leftJoins.add(join);
        return this;
    }

    public SqlBuilder noWait() {
        if (!this.forUpdate) {
            throw new RuntimeException("noWait without forUpdate cannot be called");
        } else {
            this.noWait = true;
            return this;
        }
    }

    public SqlBuilder orderBy(String name) {
        this.orderBys.add(name);
        return this;
    }

    public SqlBuilder orderBy(String name, boolean ascending) {
        if (ascending) {
            this.orderBys.add(name + " asc");
        } else {
            this.orderBys.add(name + " desc");
        }

        return this;
    }

    public String toString() {
        StringBuilder sql = new StringBuilder("select ");
        if (this.distinct) {
            sql.append("distinct ");
        }

        if (this.columns.size() == 0) {
            sql.append("*");
        } else {
            this.appendList(sql, this.columns, "", ", ");
        }

        this.appendList(sql, this.tables, " from ", ", ");
        this.appendList(sql, this.joins, " join ", " join ");
        this.appendList(sql, this.leftJoins, " left join ", " left join ");
        this.appendList(sql, this.wheres, " where ", " and ");
        this.appendList(sql, this.groupBys, " group by ", ", ");
        this.appendList(sql, this.havings, " having ", " and ");
        this.appendList(sql, this.unions, " union ", " union ");
        this.appendList(sql, this.orderBys, " order by ", ", ");
        if (this.forUpdate) {
            sql.append(" for update");
            if (this.noWait) {
                sql.append(" nowait");
            }
        }

        if (this.limit > 0 && this.offset > 0) {
            sql.append(" limit " + this.offset).append(", " + this.limit);
        } else if (this.limit > 0 && this.offset == 0) {
            sql.append(" limit " + this.limit);
        }

        return sql.toString();
    }

    public String toCountString() {
        StringBuilder sql = new StringBuilder("select COUNT(*) FROM (select ");
        if (this.distinct) {
            sql.append("distinct ");
        }

        if (this.columns.size() == 0) {
            sql.append("*");
        } else {
            this.appendList(sql, this.columns, "", ", ");
        }

        this.appendList(sql, this.tables, " from ", ", ");
        this.appendList(sql, this.joins, " join ", " join ");
        this.appendList(sql, this.leftJoins, " left join ", " left join ");
        this.appendList(sql, this.wheres, " where ", " and ");
        this.appendList(sql, this.groupBys, " group by ", ", ");
        this.appendList(sql, this.havings, " having ", " and ");
        this.appendList(sql, this.unions, " union ", " union ");
        this.appendList(sql, this.orderBys, " order by ", ", ");
        if (this.forUpdate) {
            sql.append(" for update");
            if (this.noWait) {
                sql.append(" nowait");
            }
        }

        sql.append(") AS alias");
        return sql.toString();
    }

    public SqlBuilder union(SqlBuilder unionBuilder) {
        this.unions.add(unionBuilder);
        return this;
    }

    public SqlBuilder where(String expr) {
        this.wheres.add(expr);
        return this;
    }

    private void appendList(StringBuilder sql, List<?> list, String init, String sep) {
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
}
