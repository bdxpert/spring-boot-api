package info.doula.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Detects framework bean instances and provide those instances to modules based on value provided in annotation 
 *
 * @author hossaindoula <hossaindoula@gmail.com>
 *
 */
@Retention(value=RetentionPolicy.RUNTIME)
public @interface Bean {
	String value();
}
