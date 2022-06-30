package com.github.ScipioAM.scipio_fx.persistence;

import com.github.ScipioAM.scipio_fx.persistence.annotations.Condition;
import com.github.ScipioAM.scipio_fx.persistence.annotations.UpdateNull;
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

    //=========================================== ↓↓↓↓↓↓ 供子类介入的方法 ↓↓↓↓↓↓ ===========================================

    protected void beforeAdd(DBEntity entity) {
    }

    protected void beforeUpdate(DBEntity entity) {
    }

    protected void beforeDelete(DBEntity entity) {
    }

    //=========================================== ↓↓↓↓↓↓ 初始化 ↓↓↓↓↓↓ ===========================================

    public JPAEntityDao() {
        this("jpa");
    }

    public JPAEntityDao(String persistenceUnitName) {
        entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName);
        entityManager = entityManagerFactory.createEntityManager();
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
            DBEntity dbEntity = (DBEntity) entity;
            beforeAdd(dbEntity);
            entityManager.persist(entity);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }

    /**
     * 修改数据(所有字段都会被更新，哪怕字段是null)
     *
     * @param entity 要修改的数据
     */
    public void updateWithNull(Object entity) {
        checkBeforeCUD(entity);
        EntityTransaction transaction = beginTransaction();
        try {
            DBEntity dbEntity = (DBEntity) entity;
            beforeUpdate(dbEntity);
            entityManager.merge(dbEntity);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }

    /**
     * 修改数据（跳过null的，除非）
     *
     * @param entity 要修改的数据
     * @return 受影响的行数
     */
    public int update(Object entity) throws Exception {
        EntityTransaction transaction = beginTransaction();
        try {
            //准备jpql语句
            List<EntityField> fieldList = new ArrayList<>();
            StringBuilder jpql = new StringBuilder()
                    .append("update ")
                    .append(entity.getClass().getSimpleName())
                    .append(" set ");
            StringBuilder whereSql = new StringBuilder();
            findFieldsForCU(entity, entity.getClass(), fieldList, jpql, whereSql);
            whereSql.delete(whereSql.length() - 5, whereSql.length() - 1);
            jpql.deleteCharAt(jpql.length() - 1).append(" where ").append(whereSql);
            //构建查询对象
            Query query = entityManager.createQuery(jpql.toString());
            //set参数值
            for (EntityField field : fieldList) {
                query.setParameter(field.getFieldName(), field.getValue());
            }
            //执行sql
            int affectedRows = query.executeUpdate();
            transaction.commit();
            return affectedRows;
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }

    /**
     * 删除数据
     *
     * @param entity 要删除的数据
     */
    public void delete(Object entity) {
        checkBeforeCUD(entity);
        EntityTransaction transaction = beginTransaction();
        try {
            beforeDelete((DBEntity) entity);
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
        delete(entity);
    }

    //=========================================== ↓↓↓↓↓↓ 查询 ↓↓↓↓↓↓ ===========================================

    /**
     * 根据主键查询
     *
     * @param clazz      查询的实体类
     * @param primaryKey 主键
     */
    public <T extends DBEntity> T findById(Class<T> clazz, Object primaryKey) {
        return entityManager.find(clazz, primaryKey);
    }

    /**
     * 查询全部
     */
    @SuppressWarnings("unchecked")
    public <T extends DBEntity> List<T> findAll(Class<T> clazz) {
        Query query = entityManager.createQuery("from " + clazz.getName());
        return (List<T>) query.getResultList();
    }

    /**
     * 条件查询
     */
    @SuppressWarnings("unchecked")
    public <T extends DBEntity> List<T> find(Class<T> clazz, Where where) {
        String jpql = where.buildQueryJpql(clazz.getSimpleName());
        Query query = entityManager.createQuery(jpql);
        return (List<T>) query.getResultList();
    }

    /**
     * count查询（全部字段）
     *
     * @param entityName 实体类名（不是表名）
     * @return count数
     */
    public long countAll(String entityName) {
        Query query = entityManager.createQuery("select count(*) from " + entityName);
        return (Long) query.getSingleResult();
    }

    /**
     * count查询（全部字段）
     *
     * @param entityName 实体类名称（不是表名）
     * @param fieldName  实体类里的字段名（不是DB字段名）
     * @return count数
     */
    public long count(String entityName, String fieldName) {
        Query query = entityManager.createQuery("select count(" + fieldName + ") from " + entityName);
        return (Long) query.getSingleResult();
    }

    /**
     * 查询最大值的一行数据
     *
     * @param entityName 实体类名称（不是表名）
     * @param fieldName  实体类里的字段名（不是DB字段名）
     */
    public Object findMax(String entityName, String fieldName) {
        Query query = entityManager.createQuery("select max(" + fieldName + ") from " + entityName);
        return query.getSingleResult();
    }

    /**
     * 查询最小值的一行数据
     *
     * @param entityName 实体类名称（不是表名）
     * @param fieldName  实体类里的字段名（不是DB字段名）
     */
    public Object findMin(String entityName, String fieldName) {
        Query query = entityManager.createQuery("select min(" + fieldName + ") from " + entityName);
        return query.getSingleResult();
    }

    //=========================================== ↓↓↓↓↓↓ 内部私有方法 ↓↓↓↓↓↓ ===========================================

    private void checkBeforeCUD(Object entity) throws IllegalArgumentException, ClassCastException {
        if (entity == null) {
            throw new IllegalArgumentException("entity is null");
        } else if (!entity.getClass().isAssignableFrom(DBEntity.class)) {
            throw new ClassCastException("entity type is illegal ! expect type(interface) [" + DBEntity.class + "] , actual type [" + entity.getClass().getName() + "]");
        }
    }

    /**
     * 准备update或insert的jpql语句
     */
    private void findFieldsForCU(Object instance, Class<?> clazz, List<EntityField> fieldList, StringBuilder jpql, StringBuilder whereSql) throws IllegalAccessException {
        if (clazz == Object.class) {
            return;
        }
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAnnotationPresent(Transient.class)) {
                if (field.isAnnotationPresent(Id.class) || field.isAnnotationPresent(Condition.class)) {
                    whereSql.append(field.getName()).append('=').append(':').append(field.getName()).append(" and ");
                    field.setAccessible(true);
                    Object value = field.get(instance);
                    fieldList.add(new EntityField(field.getName(), value));
                } else {
                    field.setAccessible(true);
                    Object value = field.get(instance);
                    if(value == null && !(field.isAnnotationPresent(UpdateNull.class))) {
                        //跳过null值字段
                        continue;
                    }
                    jpql.append(field.getName()).append('=').append(':').append(field.getName()).append(',');
                    fieldList.add(new EntityField(field.getName(), value));
                }
            }
        }
        findFieldsForCU(instance, clazz.getSuperclass(), fieldList, jpql, whereSql);
    }

}
