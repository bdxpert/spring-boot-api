package info.doula;

/**
 * Created by hossaindoula on 6/15/2017.
 */
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

/**
 * This class is responsible to call Spring managed Beans from non-managed Spring Beans
 */
@Configuration
public class ApplicationContextUtils implements ApplicationContextAware {

    private static ApplicationContext context;

    public static ApplicationContext getAppCtx() {
        return context;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        context = applicationContext;
    }
}
