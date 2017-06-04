package info.doula.response;

/**
 * Created by Dell on 12/16/2016.
 */
public interface Http {

    int SC_OK = 200;

    int SC_BAD_REQUEST = 400;

    int SC_NOT_FOUND = 404;

    int SC_SERVICE_UNAVAILABLE = 503;

    int SC_INTERNAL_SERVER_ERROR = 500;

    String FORM_URL_ENCODED = "www-form-urlencoded";

    String JSON_CONTENT = "application/json";

    String FORWARD_FOR = "x-forwarded-for";

    String BODY_CONTENT = "BODY_CONTENT";

    String MULTIPART_FORM = "multipart/form-data";

    String XML_MIME = "application/xml";

    String JSON_MIME = "application/json";

    String ERR_CODE_KEY = "error";

    String ERR_MSG_KEY = "error_description";

    String RESP_DATA_KEY = "responseData";

    String RESP_STATUS_KEY = "responseStatus";

}
