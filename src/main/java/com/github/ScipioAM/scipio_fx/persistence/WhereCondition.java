package com.github.ScipioAM.scipio_fx.persistence;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @since 2022/6/30
 */
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Data
public class WhereCondition {

    /** 常规的检索值 */
    private Object value;

    /** BETWEEN和NOT BETWEEN语句专用的第2个检索值 */
    private Object value2;

    /** IN和NOT IN语句专用的检索值 */
    private List<?> valueList;

    private SqlOperator operator;

    /** 是否为调用sql的函数。true：是 */
    private boolean isFunctionCall = false;

    public WhereCondition(Object value, SqlOperator operator) {
        this.value = value;
        this.operator = operator;
    }

    public String getSqlOperator() {
        if (operator != null) {
            return operator.op;
        } else {
            return null;
        }
    }

}
