package info.doula;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by doula_itc on 2017-08-15.
 */
public class PlatformUtil {

    /**
     * Get Headers from servlet request
     * @param servletRequest
     * @return headerMap
     */
    public static Map<String, String> getHeadersFromRequest(HttpServletRequest servletRequest) {

        Map<String, String> headerMap = new HashMap<>();
        Enumeration headerNames = servletRequest.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = servletRequest.getHeader(key);
            headerMap.put(key, value);
        }

        return headerMap;
    }
}
