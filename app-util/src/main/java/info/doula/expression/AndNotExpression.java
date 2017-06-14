package info.doula.expression;

/**
 * AndNotExpression
 *
 * @author hossaindoula <hossaindoula@gmail.com>
 */
public class AndNotExpression implements Expression {
	
	Expression expr = null;
	
	public AndNotExpression() {
		
	}
	
	public AndNotExpression(Expression expression) {
		expr = expression;
	}
	
	public void setExpression(Expression expression) {
		expr = expression;
	}
	
	public String toString() {
		if (expr == null)  {
			return "";
		}
		
		if (expr instanceof Expressions) {
			Expressions tmp_expr = (Expressions)expr;
			if (tmp_expr.size() > 1) {
				return "(" + expr.toString() + ")";
			}
		}
		
		return expr.toString();
	}
}
