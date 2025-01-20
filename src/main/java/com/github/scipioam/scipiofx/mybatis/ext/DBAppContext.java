package com.github.scipioam.scipiofx.mybatis.ext;

import com.github.scipioam.scipiofx.framework.AppContext;
import com.github.scipioam.scipiofx.mybatis.MybatisConfig;
import com.github.scipioam.scipiofx.mybatis.MybatisManager;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Alan Scipio
 * @since 2024/4/29
 */
@Getter
@Setter
public class DBAppContext extends AppContext {

    protected MybatisManager mybatisManager;

    protected MybatisConfig mybatisConfig;

}
