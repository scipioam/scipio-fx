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
class WhereCondition {

    private String fieldName;

    /** 常规的检索值 */
    private Object value;

    /** BETWEEN和NOT BETWEEN语句专用的第2个检索值 */
    private Object value2;

    private SqlOperator operator;

    /** 是否为update set的值 */
    private boolean isUpdateValue = false;

    public WhereCondition(String fieldName, Object value, SqlOperator operator) {
        this.fieldName = fieldName;
        this.value = value;
        this.operator = operator;
    }

    public WhereCondition(String fieldName, Object value, boolean isUpdateValue) {
        this.fieldName = fieldName;
        this.value = value;
        this.isUpdateValue = isUpdateValue;
    }

    public String getSqlOperator() {
        if (operator != null) {
            return operator.op;
        } else {
            return null;
        }
    }

}
