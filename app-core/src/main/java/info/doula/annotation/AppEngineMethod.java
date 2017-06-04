package info.doula.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Detects and call AppEngineMethod of the logic object based on value provided in annotation 
 *
 * @author hossaindoula
 *
 */
@Retention(value=RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AppEngineMethod {
	String value() default "";
}
