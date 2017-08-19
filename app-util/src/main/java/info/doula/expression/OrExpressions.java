package info.doula.expression;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * OrExpression
 *
 * @author hossaindoula <hossaindoula@gmail.com>
 */
public class OrExpressions extends Expressions {
	public OrExpressions(){
	}
	
	public OrExpressions (List<Expression> list){
		super(list);
	}
	
	public String toString() {
		if (exprs.size() > 0) {
			if (exprs.size() == 1) {
				return exprs.get(0).toString();
			}
			
			return StringUtils.join(toStringArray()," OR ");
		}

		return "";
	}
}