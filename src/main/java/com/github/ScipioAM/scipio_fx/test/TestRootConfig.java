package com.github.ScipioAM.scipio_fx.test;

import com.github.ScipioAM.scipio_fx.app.config.RootConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Alan Scipio
 * created on 2023/1/13
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TestRootConfig extends RootConfig {

    private String testA;

}
