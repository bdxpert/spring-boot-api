package info.doula;

import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Created by hossain.doula on 5/24/2016.
 */
public class UUIDFactory {
    private static final Pattern UUID_PATTERN =
            Pattern.
                    compile("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$");


    public String generateUUID() {
        return UUID.randomUUID().toString();
    }
}
