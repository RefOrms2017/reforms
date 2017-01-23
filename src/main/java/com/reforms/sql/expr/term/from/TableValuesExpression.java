package com.reforms.sql.expr.term.from;

import com.reforms.sql.expr.term.ExpressionType;
import com.reforms.sql.expr.term.FuncExpression;
import com.reforms.sql.expr.term.predicate.ValueListExpression;
import com.reforms.sql.expr.viewer.SqlBuilder;

import java.util.ArrayList;
import java.util.List;

import static com.reforms.sql.expr.term.ExpressionType.ET_TABLE_VALUES_EXPRESSION;
import static com.reforms.sql.expr.term.SqlWords.SW_AS;
import static com.reforms.sql.expr.term.SqlWords.SW_VALUES;

public class TableValuesExpression extends TableReferenceExpression {

    private List<ValueListExpression> valuesExprs = new ArrayList<>();

    private boolean asWord;

    private FuncExpression templateExpr;

    public List<ValueListExpression> getValuesExprs() {
        return valuesExprs;
    }

    public boolean addValuesExpr(ValueListExpression valuesExpr) {
        return valuesExprs.add(valuesExpr);
    }

    public void setValuesExprs(List<ValueListExpression> valuesExprs) {
        this.valuesExprs = valuesExprs;
    }

    public boolean isAsWord() {
        return asWord;
    }

    public void setAsWord(boolean asWord) {
        this.asWord = asWord;
    }

    public FuncExpression getTemplateExpr() {
        return templateExpr;
    }

    public void setTemplateExpr(FuncExpression templateExpr) {
        this.templateExpr = templateExpr;
    }

    @Override
    public ExpressionType getType() {
        return ET_TABLE_VALUES_EXPRESSION;
    }

    @Override
    public void view(SqlBuilder sqlBuilder) {
        sqlBuilder.appendSpace();
        sqlBuilder.append("(");
        sqlBuilder.appendWord(SW_VALUES);
        sqlBuilder.appendExpressions(valuesExprs, ",");
        sqlBuilder.append(")");
        if (asWord) {
            sqlBuilder.appendSpace();
            sqlBuilder.appendWord(SW_AS);
        }
        sqlBuilder.appendExpression(templateExpr);
    }
}
