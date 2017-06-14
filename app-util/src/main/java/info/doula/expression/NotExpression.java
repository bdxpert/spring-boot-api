package info.doula.expression;

/**
 * Not expression
 * It can explain NOT condition.
 * @author hossaindoula <hossaindoula@gmail.com>
 */
public class NotExpression extends AndNotExpression implements Expression {
	Expression expr = null;

	public NotExpression() {

	}

	public NotExpression(Expression expression) {
		expr = expression;
	}
}
