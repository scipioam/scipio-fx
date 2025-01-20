package com.github.scipioam.scipiofx.mybatis;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

/**
 * @author Alan Scipio
 * created on 2024/1/17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyDataSource {

    private String id;

    private SqlSessionFactory sqlSessionFactory;

    private ConnectOptions connectOptions;

    public SqlSession openSession() {
        return sqlSessionFactory.openSession();
    }

}
