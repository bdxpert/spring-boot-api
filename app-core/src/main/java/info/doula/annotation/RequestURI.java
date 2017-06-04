package info.doula.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * requestURI of the calling API
 *
 * @author hossaindoula <hossaindoula@gmail.com>
 *
 */
@Retention(value=RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface RequestURI {
	
}
