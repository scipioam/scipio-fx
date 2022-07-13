package com.github.ScipioAM.scipio_fx.persistence;

/**
 * @since 2022/6/30
 */
public enum SqlOperator {

    //TODO 待补充
    EQUAL("="),
    NOT_EQUAL("<>"),
    LESS("<"),
    LESS_EQUAL("<="),
    GREATER(">"),
    GREATER_EQUAL(">="),
    IS_NULL("is null"),
    IS_NOT_NULL("is not null"),
    ;

    public final String op;

    SqlOperator(String op) {
        this.op = op;
    }

}
