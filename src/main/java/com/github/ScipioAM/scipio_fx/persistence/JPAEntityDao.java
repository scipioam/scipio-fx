package com.github.ScipioAM.scipio_fx.persistence;

import com.github.ScipioAM.scipio_fx.utils.SeFunction;
import jakarta.persistence.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * DB操作
 *
 * @author Alan Scipio
 * @since 2020/9/30
 */
public class JPAEntityDao {

    protected final EntityManagerFactory entityManagerFactory;
    protected final EntityManager entityManager;

    //=========================================== ↓↓↓↓↓↓ 初始化 ↓↓↓↓↓↓ ===========================================

    public JPAEntityDao() {
        this("jpa");
    }

    public JPAEntityDao(String persistenceUnitName) {
        //初始化JPA
        entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName);
        entityManager = entityManagerFactory.createEntityManager();
    }

    //=========================================== ↓↓↓↓↓↓ 供子类重写的方法 ↓↓↓↓↓↓ ===========================================

    protected void beforeAdd(Object entity) {
    }

    protected void beforeUpdate(Object entity, Query query) {
    }

    protected void beforeDeleteById(Object entity) {
    }

    protected void beforeDelete(Class<?> entityType, Query query) {
    }

    //=========================================== ↓↓↓↓↓↓ 基本API ↓↓↓↓↓↓ ===========================================

    /**
     * 关闭连接
     */
    public void destroy() {
        entityManager.close();
        entityManagerFactory.close();
    }

    /**
     * 开启一个事务
     */
    public EntityTransaction beginTransaction() {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        return transaction;
    }

    public EntityManager getManager() {
        return entityManager;
    }

    public Query createJpql(String jpql) {
        return entityManager.createQuery(jpql);
    }

    public Query createJpql(String jpql, Class<?> resultClass) {
        return entityManager.createQuery(jpql, resultClass);
    }

    public Query createNativeSql(String sql) {
        return entityManager.createNativeQuery(sql);
    }

    public Query createNativeSql(String sql, Class<?> resultClass) {
        return entityManager.createNativeQuery(sql, resultClass);
    }

    //=========================================== ↓↓↓↓↓↓ 增删改 ↓↓↓↓↓↓ ===========================================

    /**
     * 新增数据
     *
     * @param entity 要新增的数据
     */
    public void add(Object entity) {
        checkBeforeCUD(entity);
        EntityTransaction transaction = beginTransaction();
        try {
            beforeAdd(entity);
            entityManager.persist(entity);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }

    /**
     * 更新数据(根据主键)
     *
     * @param entity 要修改的数据，主键根据字段上的{@link Id}注解来自动构建
     * @return 受影响的行数
     */
    public int updateById(Object entity) throws Exception {
        checkBeforeCUD(entity);
        EntityTransaction transaction = beginTransaction();
        try {
            //构建query对象
            Query query = buildUpdateQuery(entityManager, entity, null, false);
            //执行sql
            beforeUpdate(entity, query);
            int affectedRows = query.executeUpdate();
            transaction.commit();
            return affectedRows;
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }

    /**
     * 更新数据
     *
     * @param entity 要更新的数据
     * @param where  自定义where条件
     * @return 受影响的行数
     */
    public int update(Object entity, Where where) throws Exception {
        if (where == null) {
            throw new IllegalArgumentException("where condition is null");
        }
        checkBeforeCUD(entity);
        EntityTransaction transaction = beginTransaction();
        try {
            //构建query对象
            Query query = buildUpdateQuery(entityManager, entity, where, false);
            //执行sql
            beforeUpdate(entity, query);
            int affectedRows = query.executeUpdate();
            transaction.commit();
            return affectedRows;
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }


    /**
     * 更新数据(所有字段都会被更新，哪怕字段是null)
     *
     * @param entity 要修改的数据，where条件根据字段上的注解来
     */
    public int updateWithNullById(Object entity) throws Exception {
        checkBeforeCUD(entity);
        EntityTransaction transaction = beginTransaction();
        try {
            //构建query对象
            Query query = buildUpdateQuery(entityManager, entity, null, true);
            //执行sql
            beforeUpdate(entity, query);
            int affectedRows = query.executeUpdate();
            transaction.commit();
            return affectedRows;
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }

    /**
     * 更新数据(所有字段都会被更新，哪怕字段是null)
     *
     * @param entity 要修改的数据，where条件根据字段上的注解来
     */
    public int updateWithNull(Object entity, Where where) throws Exception {
        if (where == null) {
            throw new IllegalArgumentException("where condition is null");
        }
        checkBeforeCUD(entity);
        EntityTransaction transaction = beginTransaction();
        try {
            //构建query对象
            Query query = buildUpdateQuery(entityManager, entity, where, true);
            //执行sql
            beforeUpdate(entity, query);
            int affectedRows = query.executeUpdate();
            transaction.commit();
            return affectedRows;
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }

    /**
     * 删除
     *
     * @param entityType 要删除的实体类
     * @param where      where条件
     * @return 受影响的行数
     */
    public <T extends DBEntity> int delete(Class<T> entityType, Where where) {
        Query queryJpq = where.buildQuery("delete", entityManager, entityType.getSimpleName());
        beforeDelete(entityType, queryJpq);
        return queryJpq.executeUpdate();
    }

    /**
     * 根据主键删除数据
     *
     * @param entity 要删除的数据
     */
    public void deleteById(Object entity) {
        checkBeforeCUD(entity);
        EntityTransaction transaction = beginTransaction();
        try {
            beforeDeleteById(entity);
            entityManager.remove(entity);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }

    /**
     * 根据主键删除数据
     *
     * @param clazz      要删除数据的类型
     * @param primaryKey 要删除的数据的主键
     */
    public void deleteById(Class<? extends DBEntity> clazz, Object primaryKey) {
        DBEntity entity = findById(clazz, primaryKey);
        deleteById(entity);
    }

    //=========================================== ↓↓↓↓↓↓ 查询 ↓↓↓↓↓↓ ===========================================

    /**
     * 根据主键查询
     *
     * @param entityType 查询的实体类
     * @param primaryKey 主键
     */
    public <T extends DBEntity> T findById(Class<T> entityType, Object primaryKey) {
        return entityManager.find(entityType, primaryKey);
    }

    /**
     * 查询全部
     */
    @SuppressWarnings("unchecked")
    public <T extends DBEntity> List<T> findAll(Class<T> entityType) {
        Query queryJpq = entityManager.createQuery("from " + entityType.getSimpleName());
        return (List<T>) queryJpq.getResultList();
    }

    /**
     * 条件查询
     */
    @SuppressWarnings("unchecked")
    public <T extends DBEntity> List<T> find(Class<T> entityType, Where where) {
        Query queryJpq = where.buildQuery("", entityManager, entityType.getSimpleName());
        return (List<T>) queryJpq.getResultList();
    }

    /**
     * count查询（全部字段）
     *
     * @return count数
     */
    public <T extends DBEntity> long countAll(Class<T> entityType) {
        Query queryJpq = entityManager.createQuery("select count(*) from " + entityType.getSimpleName());
        return (Long) queryJpq.getSingleResult();
    }

    public <T extends DBEntity> long countAll(Class<T> entityType, Where where) {
        Query queryJpq = where.buildQuery("select count(*)", entityManager, entityType.getSimpleName());
        return (Long) queryJpq.getSingleResult();
    }

    /**
     * count查询（全部字段）
     *
     * @param fieldName 实体类里的字段名（不是DB字段名）
     * @return count数
     */
    public <T extends DBEntity> long count(Class<T> entityType, String fieldName) {
        Query queryJpq = entityManager.createQuery("select count(" + fieldName + ") from " + entityType.getSimpleName());
        return (Long) queryJpq.getSingleResult();
    }

    public <T extends DBEntity> long count(Class<T> entityType, String fieldName, Where where) {
        String prefix = "select count(" + fieldName + ")";
        Query queryJpq = where.buildQuery(prefix, entityManager, entityType.getSimpleName());
        return (Long) queryJpq.getSingleResult();
    }

    public <T extends DBEntity> long count(Class<T> entityType, SeFunction<T, ?> func) {
        return count(entityType, func.getPropertyByMethod());
    }

    public <T extends DBEntity> long count(Class<T> entityType, SeFunction<T, ?> func, Where where) {
        return count(entityType, func.getPropertyByMethod(), where);
    }

    /**
     * 查询最大值的一行数据
     *
     * @param fieldName 实体类里的字段名（不是DB字段名）
     */
    public <T extends DBEntity> Object findMax(Class<T> entityType, String fieldName) {
        Query queryJpq = entityManager.createQuery("select max(" + fieldName + ") from " + entityType.getSimpleName());
        return queryJpq.getSingleResult();
    }

    public <T extends DBEntity> Object findMax(Class<T> entityType, String fieldName, Where where) {
        String prefix = "select max(" + fieldName + ")";
        Query queryJpq = where.buildQuery(prefix, entityManager, entityType.getSimpleName());
        return queryJpq.getSingleResult();
    }

    public <T extends DBEntity> Object findMax(Class<T> entityType, SeFunction<T, ?> func) {
        return findMax(entityType, func.getPropertyByMethod());
    }

    public <T extends DBEntity> Object findMax(Class<T> entityType, SeFunction<T, ?> func, Where where) {
        return findMax(entityType, func.getPropertyByMethod(), where);
    }

    /**
     * 查询最小值的一行数据
     *
     * @param fieldName 实体类里的字段名（不是DB字段名）
     */
    public <T extends DBEntity> Object findMin(Class<T> entityType, String fieldName) {
        Query queryJpq = entityManager.createQuery("select min(" + fieldName + ") from " + entityType.getSimpleName());
        return queryJpq.getSingleResult();
    }

    public <T extends DBEntity> Object findMin(Class<T> entityType, String fieldName, Where where) {
        String prefix = "select min(" + fieldName + ")";
        Query queryJpq = where.buildQuery(prefix, entityManager, entityType.getSimpleName());
        return queryJpq.getSingleResult();
    }

    public <T extends DBEntity> Object findMin(Class<T> entityType, SeFunction<T, ?> func) {
        return findMin(entityType, func.getPropertyByMethod());
    }

    public <T extends DBEntity> Object findMin(Class<T> entityType, SeFunction<T, ?> func, Where where) {
        return findMin(entityType, func.getPropertyByMethod(), where);
    }

    //=========================================== ↓↓↓↓↓↓ 内部私有方法 ↓↓↓↓↓↓ ===========================================

    private void checkBeforeCUD(Object entity) throws IllegalArgumentException, ClassCastException {
        if (entity == null) {
            throw new IllegalArgumentException("entity is null");
        } else if (!DBEntity.class.isAssignableFrom(entity.getClass())) {
            throw new ClassCastException("entity type is illegal ! expect type(interface) [" + DBEntity.class + "] , actual type [" + entity.getClass().getName() + "]");
        }
    }

    /**
     * 构建update语句
     *
     * @param where 为null则自动构建主键where条件，否则为传入的非null自定义where条件
     */
    protected Query buildUpdateQuery(EntityManager entityManager, Object entity, Where where, boolean forceNull) throws Exception {
        Class<?> entityType = entity.getClass();
        List<WhereCondition> fieldList = new ArrayList<>();
        //构建updateJpql语句
        StringBuilder jpql = new StringBuilder()
                .append("update ")
                .append(entityType.getSimpleName())
                .append(" set ");
        if (where == null) {
            //构建主键where条件
            where = Where.build();
            findFieldsForCU(entity, entity.getClass(), fieldList, jpql, where, forceNull);
        } else {
            //自定义where条件
            findFieldsForCU(entity, entity.getClass(), fieldList, jpql, null, forceNull);
        }
        jpql.deleteCharAt(jpql.length() - 1); //删除多余的逗号
        //加上where语句
        jpql.append(where.buildWhereJpql());
        //构建查询对象
        Query query = entityManager.createQuery(jpql.toString());
        //填充value
        fieldList.addAll(where.getConditions().values());
        for (WhereCondition field : fieldList) {
            if (field.isUpdateValue()) {
                //set语句的值
                query.setParameter("_" + field.getFieldName(), field.getValue());
            } else {
                //where语句的值
                if (field.getOperator() == SqlOperator.IS_NULL || field.getOperator() == SqlOperator.IS_NOT_NULL) {
                    continue;
                }
                if (field.getOperator() == SqlOperator.BETWEEN || field.getOperator() == SqlOperator.NOT_BETWEEN) {
                    query.setParameter(field.getFieldName() + "0", field.getValue())
                            .setParameter(field.getFieldName() + "1", field.getValue());
                } else {
                    query.setParameter(field.getFieldName(), field.getValue());
                }
            }
        }
        return query;
    }

    /**
     * 准备update或insert的jpql语句
     *
     * @param primaryKeyWhere 针对主键的where条件
     * @param forceNull       强制更新null
     */
    private void findFieldsForCU(Object instance, Class<?> clazz, List<WhereCondition> fieldList, StringBuilder jpql, Where primaryKeyWhere, boolean forceNull) throws IllegalAccessException {
        if (clazz == Object.class) {
            return;
        }
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAnnotationPresent(Transient.class)) {
                field.setAccessible(true);
                Object value = field.get(instance);
                //id字段的特殊处理
                if (primaryKeyWhere != null && field.isAnnotationPresent(Id.class)) {
                    primaryKeyWhere.eq(field.getName(), value);
                }
                if (!forceNull && value == null && !(field.isAnnotationPresent(UpdateNull.class))) {
                    //跳过null值字段
                    continue;
                }
                jpql.append(field.getName()).append('=').append(':').append('_').append(field.getName()).append(',');
                fieldList.add(new WhereCondition(field.getName(), value, true));
            }
        }
        findFieldsForCU(instance, clazz.getSuperclass(), fieldList, jpql, primaryKeyWhere, forceNull);
    }

}
