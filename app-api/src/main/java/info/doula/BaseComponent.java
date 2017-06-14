package info.doula;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import info.doula.exception.BadRequestException;
import info.doula.response.Http;
import info.doula.system.impl.AppConf;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import static info.doula.util.ObjectUtils.isNullObject;

/**
 * Created by hossaindoula<hossain.doula@itconquest.com> on 2017-04-24.
 */

@Component
public abstract class BaseComponent {

    protected boolean logEnabled;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final Logger errorLogger = LoggerFactory.getLogger("ERROR");

    public BaseComponent() {
        this.logEnabled = AppConf.getInstance().getBoolean("app.logger.enabled", false);
    }

    /**
     * Return response format, default is JSON
     * @param format - requested response format
     * @return response format
     */
    protected String getResponseFormat(String format) {
        return format.equals("xml") ? "xml" : "json";
    }

    /**
     * Read request from http servlet
     * @param servletRequest
     * @return requestMap
     */
    protected Map<String,Object> readRequestFromServlet(HttpServletRequest servletRequest)
            throws BadRequestException, IOException {

        String contentType = servletRequest.getContentType();
        Map<String,Object> requestMap = new HashMap<>();
        if(logEnabled) logger.info(servletRequest.getRequestURI() + " content-type : " + contentType);
        // Check content-type and get request map
        if(contentType.contains(Http.JSON_MIME)) {
            BufferedReader reader = servletRequest.getReader();
            String body;
            String tmpStr;
            StringBuilder bodyBuilder = new StringBuilder();
            while ((tmpStr = reader.readLine()) != null) {
                bodyBuilder.append(tmpStr);
            }
            body = bodyBuilder.toString();
            try {
                Gson gson = new Gson();
                String requestBody = gson.toJson(body);
            } catch (JSONException e) {
                throw new BadRequestException("bad.formed.json");
            }

        } else { // For other content-types
            Enumeration<String> parameterNames = servletRequest.getParameterNames();
            while (parameterNames.hasMoreElements()) {
                Object parameterName = parameterNames.nextElement();
                String[] values = servletRequest.getParameterValues(parameterName.toString());

                if (values.length == 1) {
                    requestMap.put(parameterName.toString(), values[0]);
                } else {
                    requestMap.put(parameterName.toString(), values);
                }
            }
        }

        return requestMap;
    }

    /**
     * Check the content-type
     * Only POST(www-form-urlencoded, application/json), PUT, PATCH, DELETE and GET methods are supported
     * @param webRequest
     * @return true if the content-type is supported
     */
    protected boolean checkContentType(HttpServletRequest webRequest)
            throws BadRequestException {

        String method = webRequest.getMethod();
        String contentType = webRequest.getContentType();

        if (method.equals("GET")) {
            return true;

        } else if(method.equals("POST")) {
            if(!isNullObject(contentType) || contentType.contains(Http.FORM_URL_ENCODED) ||
                    contentType.contains(Http.JSON_CONTENT)) {
                return true;
            }
        } else if(method.equals("PUT")) {
            if(!isNullObject(contentType) || contentType.contains(Http.FORM_URL_ENCODED) ||
                    contentType.contains(Http.JSON_CONTENT)) {
                return true;
            }
        } else if(method.equals("PATCH")) {
            if(!isNullObject(contentType) || contentType.contains(Http.FORM_URL_ENCODED) ||
                    contentType.contains(Http.JSON_CONTENT)) {
                return true;
            }
        } else if(method.equals("DELETE")) {
            if(contentType != null) {
                return true;
            }
        } else {
            throw new BadRequestException("contentType : " + contentType + "not.support");
        }

        return false;
    }

    protected Map<String, Map<String, Object>> fromJson(Gson gson, String responseString){
        return gson.fromJson(responseString, new TypeToken<Map<String, Object>>() {
        }.getType());
    }
}
