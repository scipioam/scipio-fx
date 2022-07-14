package com.github.ScipioAM.scipio_fx.persistence;

import com.github.ScipioAM.scipio_fx.utils.SeFunction;
import com.github.ScipioAM.scipio_fx.utils.SeSupplier;
import com.github.ScipioAM.scipio_fx.utils.StringUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @since 2022/6/30
 */
public class Where {

    @Getter
    private final Map<String, WhereCondition> conditions = new LinkedHashMap<>();

    private String lastJpql = null;

    public static Where build() {
        return new Where();
    }

    /**
     * 构建{@link Query}对象
     */
    protected Query buildQuery(String prefix, EntityManager entityManager, String entityName) {
        //构建jpql语句
        String jpql;
        jpql = prefix + " from " + entityName + buildWhereJpql();
        //构建query对象
        Query query = entityManager.createQuery(jpql);
        //根据fieldName填充value
        for (WhereCondition condition : conditions.values()) {
            if (condition.getOperator() == SqlOperator.IS_NULL || condition.getOperator() == SqlOperator.IS_NOT_NULL) {
                continue;
            }
            if (condition.getOperator() == SqlOperator.BETWEEN || condition.getOperator() == SqlOperator.NOT_BETWEEN) {
                query.setParameter(condition.getFieldName() + "0", condition.getValue())
                        .setParameter(condition.getFieldName() + "1", condition.getValue());
            } else {
                query.setParameter(condition.getFieldName(), condition.getValue());
            }
        }
        return query;
    }

    /**
     * 构建where开始的jpql语句
     */
    protected String buildWhereJpql() {
        StringBuilder jpql = new StringBuilder()
                .append(" where ");
        for (WhereCondition condition : conditions.values()) {
            String fieldName = condition.getFieldName();

            //where语句开头（字段，操作符）
            jpql.append(fieldName).append(' ').append(condition.getSqlOperator()).append(' ');

            //where语句中间（值）
            //noinspection StatementWithEmptyBody
            if (condition.getOperator() == SqlOperator.IS_NULL || condition.getOperator() == SqlOperator.IS_NOT_NULL) {
                //掠过
            } else if (condition.getOperator() == SqlOperator.BETWEEN || condition.getOperator() == SqlOperator.NOT_BETWEEN) {
                jpql.append(':').append(fieldName).append('0').append(" and ").append(':').append(fieldName).append('1');
            } else {
                jpql.append(':').append(fieldName);
            }

            //where语句结尾
            jpql.append(" and ");
        }
        //去除末尾多余的and
        jpql.delete(jpql.length() - 5, jpql.length() - 1);
        //拼上自定义的结尾jpql语句
        if (StringUtils.isNotNull(lastJpql)) {
            jpql.append(' ').append(lastJpql);
        }
        return jpql.toString();
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

    //=========================================== ↓↓↓↓↓↓ 其他SQL ↓↓↓↓↓↓ ===========================================

    public Where lastJpql(String last) {
        this.lastJpql = last;
        return this;
    }

    /**
     * 排序（正序）， 会覆盖lastJpql
     */
    public Where orderByAsc(String... fieldNames) {
        StringBuilder last = new StringBuilder();
        last.append("order by ");
        for (String fieldName : fieldNames) {
            last.append(fieldName).append(',');
        }
        last.deleteCharAt(last.length() - 1);
        this.lastJpql = last.toString();
        return this;
    }

    /**
     * 排序（倒序）， 会覆盖lastJpql
     */
    public Where orderByDesc(String... fieldNames) {
        StringBuilder last = new StringBuilder();
        last.append("order by ");
        for (String fieldName : fieldNames) {
            last.append(fieldName).append(" desc,");
        }
        last.delete(last.length() - 6, last.length() - 1);
        this.lastJpql = last.toString();
        return this;
    }

    //=========================================== ↓↓↓↓↓↓ 条件 ↓↓↓↓↓↓ ===========================================

    /**
     * where条件
     *
     * @param fieldName 实体类字段名（不是DB字段名）
     */
    public Where condition(String fieldName, Object value, SqlOperator operator) {
        conditions.put(fieldName, new WhereCondition(fieldName, value, operator));
        return this;
    }

    /**
     * where条件(lambda)
     *
     * @param func 根据方法名，会自动转为实体类字段名（不是DB字段名）
     */
    public <T extends DBEntity> Where condition(SeFunction<T, ?> func, Object value, SqlOperator operator) {
        String fieldName = func.getPropertyByMethod();
        conditions.put(fieldName, new WhereCondition(fieldName, value, operator));
        return this;
    }

    /**
     * where条件(lambda)
     *
     * @param supplier 根据方法名，会自动转为实体类字段名（不是DB字段名），且其返回值就是条件的value
     */
    public Where condition(SeSupplier<?> supplier, SqlOperator operator) {
        String fieldName = supplier.getPropertyByMethod();
        Object value = supplier.get();
        conditions.put(fieldName, new WhereCondition(fieldName, value, operator));
        return this;
    }

    /**
     * 等于
     *
     * @param fieldName 实体类字段名（不是DB字段名）
     */
    public Where eq(String fieldName, Object value) {
        conditions.put(fieldName, new WhereCondition(fieldName, value, SqlOperator.EQUAL));
        return this;
    }

    /**
     * 等于(lambda)
     *
     * @param func 根据方法名，会自动转为实体类字段名（不是DB字段名）
     */
    public <T extends DBEntity> Where eq(SeFunction<T, ?> func, Object value) {
        String fieldName = func.getPropertyByMethod();
        conditions.put(fieldName, new WhereCondition(fieldName, value, SqlOperator.EQUAL));
        return this;
    }

    /**
     * 等于(lambda)
     *
     * @param supplier 根据方法名，会自动转为实体类字段名（不是DB字段名），且其返回值就是条件的value
     */
    public Where eq(SeSupplier<?> supplier) {
        String fieldName = supplier.getPropertyByMethod();
        Object value = supplier.get();
        conditions.put(fieldName, new WhereCondition(fieldName, value, SqlOperator.EQUAL));
        return this;
    }

    /**
     * 不等于
     *
     * @param fieldName 实体类字段名（不是DB字段名）
     */
    public Where ne(String fieldName, Object value) {
        conditions.put(fieldName, new WhereCondition(fieldName, value, SqlOperator.NOT_EQUAL));
        return this;
    }

    /**
     * 不等于(lambda)
     *
     * @param func 根据方法名，会自动转为实体类字段名（不是DB字段名）
     */
    public <T extends DBEntity> Where ne(SeFunction<T, ?> func, Object value) {
        String fieldName = func.getPropertyByMethod();
        conditions.put(fieldName, new WhereCondition(fieldName, value, SqlOperator.NOT_EQUAL));
        return this;
    }

    /**
     * 不等于(lambda)
     *
     * @param supplier 根据方法名，会自动转为实体类字段名（不是DB字段名），且其返回值就是条件的value
     */
    public Where ne(SeSupplier<?> supplier) {
        String fieldName = supplier.getPropertyByMethod();
        Object value = supplier.get();
        conditions.put(fieldName, new WhereCondition(fieldName, value, SqlOperator.NOT_EQUAL));
        return this;
    }

    /**
     * 小于
     *
     * @param fieldName 实体类字段名（不是DB字段名）
     */
    public Where lt(String fieldName, Object value) {
        conditions.put(fieldName, new WhereCondition(fieldName, value, SqlOperator.LESS));
        return this;
    }

    /**
     * 小于(lambda)
     *
     * @param func 根据方法名，会自动转为实体类字段名（不是DB字段名）
     */
    public <T extends DBEntity> Where lt(SeFunction<T, ?> func, Object value) {
        String fieldName = func.getPropertyByMethod();
        conditions.put(fieldName, new WhereCondition(fieldName, value, SqlOperator.LESS));
        return this;
    }

    /**
     * 小于(lambda)
     *
     * @param supplier 根据方法名，会自动转为实体类字段名（不是DB字段名），且其返回值就是条件的value
     */
    public Where lt(SeSupplier<?> supplier) {
        String fieldName = supplier.getPropertyByMethod();
        Object value = supplier.get();
        conditions.put(fieldName, new WhereCondition(fieldName, value, SqlOperator.LESS));
        return this;
    }

    /**
     * 小于等于
     *
     * @param fieldName 实体类字段名（不是DB字段名）
     */
    public Where le(String fieldName, Object value) {
        conditions.put(fieldName, new WhereCondition(fieldName, value, SqlOperator.LESS_EQUAL));
        return this;
    }

    /**
     * 小于等于(lambda)
     *
     * @param func 根据方法名，会自动转为实体类字段名（不是DB字段名）
     */
    public <T extends DBEntity> Where le(SeFunction<T, ?> func, Object value) {
        String fieldName = func.getPropertyByMethod();
        conditions.put(fieldName, new WhereCondition(fieldName, value, SqlOperator.LESS_EQUAL));
        return this;
    }

    /**
     * 小于等于(lambda)
     *
     * @param supplier 根据方法名，会自动转为实体类字段名（不是DB字段名），且其返回值就是条件的value
     */
    public Where le(SeSupplier<?> supplier) {
        String fieldName = supplier.getPropertyByMethod();
        Object value = supplier.get();
        conditions.put(fieldName, new WhereCondition(fieldName, value, SqlOperator.LESS_EQUAL));
        return this;
    }

    /**
     * 大于
     *
     * @param fieldName 实体类字段名（不是DB字段名）
     */
    public Where gt(String fieldName, Object value) {
        conditions.put(fieldName, new WhereCondition(fieldName, value, SqlOperator.GREATER));
        return this;
    }

    /**
     * 大于(lambda)
     *
     * @param func 根据方法名，会自动转为实体类字段名（不是DB字段名）
     */
    public <T extends DBEntity> Where gt(SeFunction<T, ?> func, Object value) {
        String fieldName = func.getPropertyByMethod();
        conditions.put(fieldName, new WhereCondition(fieldName, value, SqlOperator.GREATER));
        return this;
    }

    /**
     * 大于(lambda)
     *
     * @param supplier 根据方法名，会自动转为实体类字段名（不是DB字段名），且其返回值就是条件的value
     */
    public Where gt(SeSupplier<?> supplier) {
        String fieldName = supplier.getPropertyByMethod();
        Object value = supplier.get();
        conditions.put(fieldName, new WhereCondition(fieldName, value, SqlOperator.GREATER));
        return this;
    }

    /**
     * 大于等于
     *
     * @param fieldName 实体类字段名（不是DB字段名）
     */
    public Where ge(String fieldName, Object value) {
        conditions.put(fieldName, new WhereCondition(fieldName, value, SqlOperator.GREATER_EQUAL));
        return this;
    }

    /**
     * 大于等于(lambda)
     *
     * @param func 根据方法名，会自动转为实体类字段名（不是DB字段名）
     */
    public <T extends DBEntity> Where ge(SeFunction<T, ?> func, Object value) {
        String fieldName = func.getPropertyByMethod();
        conditions.put(fieldName, new WhereCondition(fieldName, value, SqlOperator.GREATER_EQUAL));
        return this;
    }

    /**
     * 大于等于(lambda)
     *
     * @param supplier 根据方法名，会自动转为实体类字段名（不是DB字段名），且其返回值就是条件的value
     */
    public Where ge(SeSupplier<?> supplier) {
        String fieldName = supplier.getPropertyByMethod();
        Object value = supplier.get();
        conditions.put(fieldName, new WhereCondition(fieldName, value, SqlOperator.GREATER_EQUAL));
        return this;
    }

    /**
     * where语句：字段 is null
     *
     * @param fieldName 实体类字段名（不是DB字段名）
     */
    public Where isNull(String fieldName) {
        conditions.put(fieldName, new WhereCondition(fieldName, null, SqlOperator.GREATER_EQUAL));
        return this;
    }

    /**
     * where语句：字段 is not null
     *
     * @param fieldName 实体类字段名（不是DB字段名）
     */
    public Where isNotNull(String fieldName) {
        conditions.put(fieldName, new WhereCondition(fieldName, null, SqlOperator.GREATER_EQUAL));
        return this;
    }

    /**
     * LIKE查询
     *
     * @param fieldName 实体类字段名（不是DB字段名）
     */
    public Where like(String fieldName, Object value) {
        conditions.put(fieldName, new WhereCondition(fieldName, value, SqlOperator.LIKE));
        return this;
    }

    /**
     * LIKE查询(lambda)
     *
     * @param func 根据方法名，会自动转为实体类字段名（不是DB字段名）
     */
    public <T extends DBEntity> Where like(SeFunction<T, ?> func, Object value) {
        String fieldName = func.getPropertyByMethod();
        conditions.put(fieldName, new WhereCondition(fieldName, value, SqlOperator.LIKE));
        return this;
    }

    /**
     * LIKE查询(lambda)
     *
     * @param supplier 根据方法名，会自动转为实体类字段名（不是DB字段名），且其返回值就是条件的value
     */
    public Where like(SeSupplier<?> supplier) {
        String fieldName = supplier.getPropertyByMethod();
        Object value = supplier.get();
        conditions.put(fieldName, new WhereCondition(fieldName, value, SqlOperator.LIKE));
        return this;
    }

    /**
     * NOT LIKE查询
     *
     * @param fieldName 实体类字段名（不是DB字段名）
     */
    public Where notLike(String fieldName, Object value) {
        conditions.put(fieldName, new WhereCondition(fieldName, value, SqlOperator.NOT_LIKE));
        return this;
    }

    /**
     * NOT LIKE查询(lambda)
     *
     * @param func 根据方法名，会自动转为实体类字段名（不是DB字段名）
     */
    public <T extends DBEntity> Where notLike(SeFunction<T, ?> func, Object value) {
        String fieldName = func.getPropertyByMethod();
        conditions.put(fieldName, new WhereCondition(fieldName, value, SqlOperator.NOT_LIKE));
        return this;
    }

    /**
     * NOT LIKE查询(lambda)
     *
     * @param supplier 根据方法名，会自动转为实体类字段名（不是DB字段名），且其返回值就是条件的value
     */
    public Where notLike(SeSupplier<?> supplier) {
        String fieldName = supplier.getPropertyByMethod();
        Object value = supplier.get();
        conditions.put(fieldName, new WhereCondition(fieldName, value, SqlOperator.NOT_LIKE));
        return this;
    }

    /**
     * BETWEEN查询
     *
     * @param fieldName 实体类字段名（不是DB字段名）
     */
    public Where between(String fieldName, Object value, Object value2) {
        conditions.put(fieldName, new WhereCondition(fieldName, value, SqlOperator.BETWEEN).setValue2(value2));
        return this;
    }

    /**
     * BETWEEN查询(lambda)
     *
     * @param func 根据方法名，会自动转为实体类字段名（不是DB字段名）
     */
    public <T extends DBEntity> Where between(SeFunction<T, ?> func, Object value, Object value2) {
        String fieldName = func.getPropertyByMethod();
        conditions.put(fieldName, new WhereCondition(fieldName, value, SqlOperator.BETWEEN).setValue2(value2));
        return this;
    }

    /**
     * NOT BETWEEN查询
     *
     * @param fieldName 实体类字段名（不是DB字段名）
     */
    public Where notBetween(String fieldName, Object value, Object value2) {
        conditions.put(fieldName, new WhereCondition(fieldName, value, SqlOperator.NOT_BETWEEN).setValue2(value2));
        return this;
    }

    /**
     * NOT BETWEEN查询(lambda)
     *
     * @param func 根据方法名，会自动转为实体类字段名（不是DB字段名）
     */
    public <T extends DBEntity> Where notBetween(SeFunction<T, ?> func, Object value, Object value2) {
        String fieldName = func.getPropertyByMethod();
        conditions.put(fieldName, new WhereCondition(fieldName, value, SqlOperator.NOT_BETWEEN).setValue2(value2));
        return this;
    }

    /**
     * BETWEEN查询
     *
     * @param fieldName 实体类字段名（不是DB字段名）
     */
    public Where in(String fieldName, List<?> valueList) {
        conditions.put(fieldName, new WhereCondition()
                .setFieldName(fieldName)
                .setOperator(SqlOperator.IN)
                .setValue(valueList));
        return this;
    }

    /**
     * BETWEEN查询(lambda)
     *
     * @param func 根据方法名，会自动转为实体类字段名（不是DB字段名）
     */
    public <T extends DBEntity> Where in(SeFunction<T, ?> func, List<?> valueList) {
        String fieldName = func.getPropertyByMethod();
        conditions.put(fieldName, new WhereCondition()
                .setFieldName(fieldName)
                .setOperator(SqlOperator.IN)
                .setValue(valueList));
        return this;
    }

    /**
     * NOT BETWEEN查询
     *
     * @param fieldName 实体类字段名（不是DB字段名）
     */
    public Where notIn(String fieldName, List<?> valueList) {
        conditions.put(fieldName, new WhereCondition()
                .setFieldName(fieldName)
                .setOperator(SqlOperator.NOT_IN)
                .setValue(valueList));
        return this;
    }

    /**
     * NOT BETWEEN查询(lambda)
     *
     * @param func 根据方法名，会自动转为实体类字段名（不是DB字段名）
     */
    public <T extends DBEntity> Where notIn(SeFunction<T, ?> func, List<?> valueList) {
        String fieldName = func.getPropertyByMethod();
        conditions.put(fieldName, new WhereCondition()
                .setFieldName(fieldName)
                .setOperator(SqlOperator.NOT_IN)
                .setValue(valueList));
        return this;
    }

}
