package com.github.ScipioAM.scipio_fx.test.bean;

import com.github.ScipioAM.scipio_fx.table.annotations.TableColumnBind;
import lombok.Data;

/**
 * @since 2022/6/30
 */
@Data
public class Human {

    @TableColumnBind(title = "RACE")
    private String race;

    private String a;

}
