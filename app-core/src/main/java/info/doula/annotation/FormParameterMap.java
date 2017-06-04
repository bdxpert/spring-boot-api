package info.doula.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by hossaindoula<hossain.doula@itconquest.com> on 2017-04-25.
 */

@Retention(value= RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface FormParameterMap {
}
