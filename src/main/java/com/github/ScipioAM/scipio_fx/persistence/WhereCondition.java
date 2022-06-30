package com.github.ScipioAM.scipio_fx.persistence;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @since 2022/6/30
 */
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Data
public class WhereCondition {

    private Object value;

    private SqlOperator operator;

    public String getSqlOperator() {
        if (operator != null) {
            return operator.op;
        } else {
            return null;
        }
    }

}
