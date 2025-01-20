package com.github.scipioam.scipiofx.mybatis.ext;

import com.github.scipioam.scipiofx.framework.config.ConfigRootProperties;
import com.github.scipioam.scipiofx.mybatis.MybatisConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Alan Scipio
 * @since 2024/4/30
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DBConfigProperties extends ConfigRootProperties {

    protected MybatisConfig mybatis;

}
