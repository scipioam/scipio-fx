package com.github.ScipioAM.scipio_fx.persistence;

import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @since 2022/6/30
 */
public class Where {

    @Getter
    private final Map<String, WhereCondition> conditions = new LinkedHashMap<>();

    public String buildQueryJpql(String entityName) {
        StringBuilder hql = new StringBuilder()
                .append("from ")
                .append(entityName)
                .append(" where ");
        for (Map.Entry<String, WhereCondition> entry : conditions.entrySet()) {
            WhereCondition condition = entry.getValue();
            hql.append(entry.getKey()).append(condition.getSqlOperator()).append(condition.getValue()).append(" and ");
        }
        hql.delete(hql.length() - 5, hql.length() - 1);
        return hql.toString();
    }

    public static Where build() {
        return new Where();
    }

    //=========================================== ↓↓↓↓↓↓ 其他操作 ↓↓↓↓↓↓ ===========================================

    public WhereCondition getCondition(String fieldName) {
        return conditions.get(fieldName);
    }

    public Where removeCondition(String fieldName) {
        conditions.remove(fieldName);
        return this;
    }

    public int conditionsCount() {
        return conditions.size();
    }

    public boolean containsConditionByKey(String fieldName) {
        return conditions.containsKey(fieldName);
    }

    //=========================================== ↓↓↓↓↓↓ 条件 ↓↓↓↓↓↓ ===========================================

    /**
     * where条件
     *
     * @param fieldName 实体类字段名（不是DB字段名）
     */
    public Where condition(String fieldName, Object value, SqlOperator operator) {
        conditions.put(fieldName, new WhereCondition(value, operator));
        return this;
    }

    /**
     * 等于
     *
     * @param fieldName 实体类字段名（不是DB字段名）
     */
    public Where eq(String fieldName, Object value) {
        conditions.put(fieldName, new WhereCondition(value, SqlOperator.EQUAL));
        return this;
    }

    /**
     * 不等于
     *
     * @param fieldName 实体类字段名（不是DB字段名）
     */
    public Where ne(String fieldName, Object value) {
        conditions.put(fieldName, new WhereCondition(value, SqlOperator.NOT_EQUAL));
        return this;
    }

    /**
     * 小于
     *
     * @param fieldName 实体类字段名（不是DB字段名）
     */
    public Where lt(String fieldName, Object value) {
        conditions.put(fieldName, new WhereCondition(value, SqlOperator.LESS));
        return this;
    }

    /**
     * 小于等于
     *
     * @param fieldName 实体类字段名（不是DB字段名）
     */
    public Where le(String fieldName, Object value) {
        conditions.put(fieldName, new WhereCondition(value, SqlOperator.LESS_EQUAL));
        return this;
    }

    /**
     * 大于
     *
     * @param fieldName 实体类字段名（不是DB字段名）
     */
    public Where gt(String fieldName, Object value) {
        conditions.put(fieldName, new WhereCondition(value, SqlOperator.GREATER));
        return this;
    }

    /**
     * 大于等于
     *
     * @param fieldName 实体类字段名（不是DB字段名）
     */
    public Where ge(String fieldName, Object value) {
        conditions.put(fieldName, new WhereCondition(value, SqlOperator.GREATER_EQUAL));
        return this;
    }

}
