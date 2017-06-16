package info.doula.tenant;

/**
 * Created by hossaindoula on 6/16/2017.
 */
public class TenantContext {

    public static final String DEFAULT_TENANT = "test";

    private static ThreadLocal<String> currentTenant = ThreadLocal.withInitial(() -> DEFAULT_TENANT);

    public static void setCurrentTenant(String tenant) {
        currentTenant.set(tenant);
    }

    public static String getCurrentTenant() {
        return currentTenant.get();
    }

    public static void clear() {
        currentTenant.remove();
    }

}
