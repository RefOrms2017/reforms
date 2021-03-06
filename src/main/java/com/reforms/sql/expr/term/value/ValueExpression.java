package com.reforms.sql.expr.term.value;

import com.reforms.sql.expr.term.ExpressionType;
import com.reforms.sql.expr.term.SelectableExpression;
import com.reforms.sql.expr.viewer.SqlBuilder;

import static com.reforms.sql.expr.term.ExpressionType.ET_VALUE_EXPRESSION;

public class ValueExpression extends SelectableExpression {

    private final String value;

    private final ValueExpressionType valueExprType;

    public ValueExpression(String value, ValueExpressionType valueExprType) {
        this.value = value;
        this.valueExprType = valueExprType;
    }

    public String getValue() {
        return value;
    }

    public ValueExpressionType getValueExprType() {
        return valueExprType;
    }

    @Override
    public ExpressionType getType() {
        return ET_VALUE_EXPRESSION;
    }

    @Override
    public void view(SqlBuilder sqlBuilder) {
        sqlBuilder.appendWord(value);
    }
}