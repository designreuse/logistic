package com.frame.web.base.baseRepository.builder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HqlBuilder {
    private String table;
    private String tableAlias;
    private List<String> wheres = new ArrayList();
    private List<String> groupBys = new ArrayList();
    private List<String> orderBys = new ArrayList();

    public HqlBuilder(String table, String alias) {
        this.table = table;
        this.tableAlias = alias;
    }

    public HqlBuilder groupBy(String expr) {
        this.groupBys.add(this.tableAlias + "." + expr);
        return this;
    }

    public HqlBuilder orderBy(String name) {
        this.orderBys.add(this.tableAlias + "." + name);
        return this;
    }

    public HqlBuilder orderBy(String name, boolean ascending) {
        if (ascending) {
            this.orderBys.add(this.tableAlias + "." + name + " asc");
        } else {
            this.orderBys.add(this.tableAlias + "." + name + " desc");
        }
        return this;
    }

    public String toString() {
        StringBuilder sql = new StringBuilder("from ").append(this.table).append(" ").append(this.tableAlias);
        this.appendList(sql, this.wheres, " where ", " and ");
        this.appendList(sql, this.groupBys, " group by ", ", ");
        this.appendList(sql, this.orderBys, " order by ", ", ");
        return sql.toString();
    }

    public String toCountString() {
        StringBuilder sql = new StringBuilder("select count(*) from ").append(this.table).append(" ").append(this.tableAlias);
        this.appendList(sql, this.wheres, " where ", " and ");
        this.appendList(sql, this.groupBys, " group by ", ", ");
        this.appendList(sql, this.orderBys, " order by ", ", ");
        return sql.toString();
    }

    public HqlBuilder where(String expr) {
        this.wheres.add(this.tableAlias + "." + expr);
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
