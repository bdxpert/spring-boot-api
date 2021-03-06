package info.doula.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Provides request parameter for the current request based on annotation value
 *
 * @author hossaindoula <hossaindoula@gmail.com>
 * 
 */
@Retention(value=RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Parameter {
	String value();
}
