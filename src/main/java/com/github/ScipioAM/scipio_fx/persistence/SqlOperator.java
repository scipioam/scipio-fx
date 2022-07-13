package com.github.ScipioAM.scipio_fx.persistence;

/**
 * @since 2022/6/30
 */
public enum SqlOperator {

    EQUAL("="),
    NOT_EQUAL("<>"),
    LESS("<"),
    LESS_EQUAL("<="),
    GREATER(">"),
    GREATER_EQUAL(">="),
    IS_NULL("is null"),
    IS_NOT_NULL("is not null"),
    LIKE("like"),
    NOT_LIKE("not like"),
    BETWEEN("between"),
    NOT_BETWEEN("not between"),
    IN("in"),
    NOT_IN("not in"),
    ;

    public final String op;

    SqlOperator(String op) {
        this.op = op;
    }

}
