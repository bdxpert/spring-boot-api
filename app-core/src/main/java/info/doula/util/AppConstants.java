package info.doula.util;

import java.util.Date;

/**
 * Basic Constants where no method need not to be present
 * It is used for basic configuration or data related fact
 * Created by hossain.doula on 5/15/2016.
 */
public interface AppConstants {

    String BASE_NAME_SPACE = "info.doula";
    String SEC_NAME_SPACE = BASE_NAME_SPACE + ".security";

    String SESSION_USER = "sessionUser";
    String SESSION_USER_ID = "sessionUserId";
    String SESSION_USER_AUTHORIZED_GROUPS = "sessionUserAuthorizedGroups";
    String SESSION_USER_AUTHORITIES = "sessionUserAuthorities";
    String SESSION_USER_FEATURES = "sessionUserFeatures";
    String SESSION_SAVE = "save";
    String DASHBOARD_LINK = "Dashboard";

    String MODEL_STUB = "model";
    String DAO_STUB = "dao";

    String APP_NAME = "RESTApp";
    String APPLICATION_NAME = "RESTFul WebApp";
    String SEPARATOR = "-";
    String COMPANY_NAME = "doula";
    String ADDRESS = "Github";
    String SUCCESS_MESSAGE = "success";
    String FAILED_MESSAGE = "failed";
    String SB_KEY = "doula-key";

    String TEMPLATE_LOADER_PATH = "classpath:templates";
    String VIEW_PREFIX = "/WEB-INF/views/";
    String VIEW_SUFFIX = ".ftl";
    String RC_CTX_ATTR = "rc";
    String RESOURCE_HANDLER = "/assets/**";
    String RESOURCE_LOCATION = "/assets/";

    String EXCLUDE_FILTERED_URI = "/";
    String LOGIN_URI = "/";
    String UNAUTHORIZED_URI = "/unauthorized";

    String COMPANY_COPYRIGHT_TEXT = DateUtils.getYear(new Date()) + " " +  COMPANY_NAME + " . All Rights Reserved";

    String NUM_F_ERR = "number format error";
    String SEC_ERR = "security error";
    String ILL_ARG_ERR = "illegal argument error";
    String ILL_ACC_ERR = "illegal access error";
    String ILL_DAT_PAR_ERR = "internal data parse error";

    String RESP_LOG_ERR = "response logging error";

    String PR_MST_INT = "parameter should be integer";
    String OPT_MST = "options should not be null/blank";
    String PR_MST = "parameter should not be null/blank";
    String PR_MST_DEC = "parameter should be decimal";
    String PR_MST_LNG = "parameter should be long";
    String PR_MST_NMBR = "parameter should be numbers";
    String ARR_MST_UND = "array size must be under";
    String LEN_MST_UND = "length must be under";
    String LEN_MST_OV = "length must be over";
    String MST_FLLW = "must follow";
    String MST_UND = "must be under";
    String MST_OV = "must be over";
    String PR_MST_LST = "parameters must be list";
    String UNKNOWN_TYPE = "unknown type";

}
