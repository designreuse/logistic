package com.frame.core.sql;

public class RefRule<T> {

    public RefRule(T entity, Rule rule) {
        this.entity = entity;
        this.rule = rule;
    }

    private T entity;

    private Rule rule;

    public T getEntity() {
        return entity;
    }

    public void setEntity(T entity) {
        this.entity = entity;
    }

    public Rule getRule() {
        return rule;
    }

    public void setRule(Rule rule) {
        this.rule = rule;
    }

    public enum Rule {
        INSERT("INSERT"), DELETE("DELETE"), UPDATE("UPDATE"), SELECT("SELECT");
        private String val;

        private Rule(String val) {
            this.val = val;
        }
    }
}
