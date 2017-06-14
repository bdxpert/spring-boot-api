package info.doula.expression;

import static info.doula.util.ObjectUtils.isNullObject;

/**
 * StringExpression
 *
 * @author hossaindoula <hossaindoula@gmail.com>
 */
public class StringExpression implements Expression {
	String expr = null;
	
	public StringExpression() {
		
	}
	
	public StringExpression(Expression expression) {
		expr = expression.toString();
	}
	
	public StringExpression(String string) {
		expr = string;
	}
	
	public void setExpression(Expression expression) {
		expr = expression.toString();
	}
	
	public String toString() {
		if (isNullObject(expr)) return "";
		return expr;
	}
}
