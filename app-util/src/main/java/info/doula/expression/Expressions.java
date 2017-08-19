package info.doula.expression;

/**
 * @author hossaindoula <hossaindoula@gmail.com>
 */
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static info.doula.ObjectUtils.isNullObject;

public class Expressions implements Expression {
    public List<Expression> exprs = new ArrayList<>();

    public Expressions () {
    }

    public Expressions (Expression expression) {
        exprs.add(expression);
    }

    public Expressions (List<Expression> list) {
        exprs = list;
    }

    public int size() {
        return exprs.size();
    }

    public void addQuery(Expression expression) {
        exprs.add(expression);
    }

    public void addQuery(String string) {
        StringExpression expression = new StringExpression(string);
        exprs.add(expression);
    }

    public String[] toStringArray(){
        int count = exprs.size();
        String[] ret = new String[count];
        for (int i = 0; i < count; i++) {
            ret[i] = exprs.get(i).toString();
        }

        return ret;
    }

    public String toString() {
        ArrayList<String> result = new ArrayList<String>();
        for (int i = 0; i < exprs.size(); i++) {
            Expression expr = exprs.get(i);

            if (isNullObject(expr) || expr.toString().equals("")) continue;

            // add an expression
            if (expr instanceof Expressions) {
                Expressions tmp_expr = (Expressions)expr;
                if (tmp_expr.size() > 1) {
                    result.add("(" + expr.toString() + ")");
                } else {
                    result.add(expr.toString());
                }
            } else {
                result.add(expr.toString());
            }

            // add AND, ANDNOT or NOT
            if (i < (exprs.size() - 1)) {
                Expression nextExpr = exprs.get(i + 1);
                if (!isNullObject(nextExpr) && !nextExpr.toString().equals("")) {
                    if (nextExpr instanceof NotExpression) {
                        result.add("NOT");
                    } else if (nextExpr instanceof AndNotExpression) {
                        result.add("ANDNOT");
                    } else {
                        result.add("AND");
                    }
                }
            }
        }
        String[] ret = new String[result.size()];
        return StringUtils.join(result.toArray(ret)," ");
    }
}
