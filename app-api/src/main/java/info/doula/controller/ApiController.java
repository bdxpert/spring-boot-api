package info.doula.controller;

import info.doula.BaseComponent;
import info.doula.exception.BadRequestException;
import info.doula.exception.NotFoundException;
import info.doula.exception.ServiceConditionException;
import info.doula.exception.SystemException;
import info.doula.response.Http;
import info.doula.service.ApiExecuteService;
import info.doula.system.ResponseLogger;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hossaindoula<hossain.doula@itconquest.com> on 2017-04-24.
 */
@RestController
@Slf4j
public class ApiController extends BaseComponent {

    private ResponseLogger responseLogger = new ResponseLogger();

    @Autowired
    ApiExecuteService apiExecuteService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final Logger errorLogger = LoggerFactory.getLogger("ERROR");

    /************************ POST *********************************/
    /**
     * Used for all the Version API's
     *
     * @param servletRequest - servlet request
     * @param serviceName    - module name/ action class bean name
     * @param versionNumber  - version number for the corresponding API
     * @return response format
     */
    @RequestMapping(value = "/api/{service}/{operation}/{version}",
                    produces = {Http.XML_MIME, Http.JSON_MIME},
                    method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> executeVersionApiWithPost(HttpServletRequest servletRequest,
                                                    @PathVariable("service") String serviceName,
                                                    @PathVariable("operation") String operationName,
                                                    @PathVariable("version") String versionNumber)
            throws BadRequestException,
            NotFoundException,
            SystemException,
            ServiceConditionException, IOException {
        Map<String, Object> response = execute(servletRequest, serviceName, operationName);
        log.info("get method");

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Used for all the Version API's
     *
     * @param servletRequest - servlet request
     * @param serviceName    - module name/ action class bean name
     * @return response format
     */
    @RequestMapping(value = "/api/{service}/{operation}",
                    produces = {Http.XML_MIME, Http.JSON_MIME},
                    method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> executePassThroughApiWithPost(HttpServletRequest servletRequest,
                                                    @PathVariable("service") String serviceName,
                                                    @PathVariable("operation") String operationName)
            throws BadRequestException, NotFoundException, SystemException, ServiceConditionException, IOException {
        Map<String, Object> response = execute(servletRequest, serviceName, operationName);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    /************************ POST *********************************/

    /************************ GET *********************************/
    /**
     * Used for all the Version API's
     *
     * @param servletRequest - servlet request
     * @param serviceName    - module name/ action class bean name
     * @param versionNumber  - version number for the corresponding API
     * @return response format
     */
    @RequestMapping(value = "/api/{service}/{operation}/{version}",
                    produces = {Http.XML_MIME, Http.JSON_MIME},
                    method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> executeVersionApiWithGet(HttpServletRequest servletRequest,
                                                    @PathVariable("service") String serviceName,
                                                    @PathVariable("operation") String operationName,
                                                    @PathVariable("version") String versionNumber)
            throws BadRequestException, NotFoundException, SystemException, ServiceConditionException, IOException {
        Map<String, Object> response = execute(servletRequest, serviceName, operationName);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Used for all the Version API's
     *
     * @param servletRequest - servlet request
     * @param serviceName    - module name/ action class bean name
     * @return response format
     */
    @RequestMapping(value = "/api/{service}/{operation}",
                    produces = {Http.XML_MIME, Http.JSON_MIME},
                    method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> executePassThroughApiWithGet(HttpServletRequest servletRequest,
                                                    @PathVariable("service") String serviceName,
                                                    @PathVariable("operation") String operationName)
            throws BadRequestException, NotFoundException, SystemException, ServiceConditionException, IOException {
        Map<String, Object> response = execute(servletRequest, serviceName, operationName);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    /************************ GET *********************************/

    /************************ PUT *********************************/
    /**
     * Used for all the Version API's
     *
     * @param servletRequest - servlet request
     * @param serviceName    - module name/ action class bean name
     * @param versionNumber  - version number for the corresponding API
     * @return response format
     */
    @RequestMapping(value = "/api/{service}/{operation}/{version}",
                    produces = {Http.XML_MIME, Http.JSON_MIME},
                    method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> executeVersionApiWithPut(HttpServletRequest servletRequest,
                                                    @PathVariable("service") String serviceName,
                                                    @PathVariable("operation") String operationName,
                                                    @PathVariable("version") String versionNumber)
            throws BadRequestException, NotFoundException, SystemException, ServiceConditionException, IOException {
        Map<String, Object> response = execute(servletRequest, serviceName, operationName);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Used for all the Version API's
     *
     * @param servletRequest - servlet request
     * @param serviceName    - module name/ action class bean name
     * @return response format
     */
    @RequestMapping(value = "/api/{service}/{operation}",
                    produces = {Http.XML_MIME, Http.JSON_MIME},
                    method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> executePassThroughApiWithPut(HttpServletRequest servletRequest,
                                                    @PathVariable("service") String serviceName,
                                                    @PathVariable("operation") String operationName)
            throws BadRequestException, NotFoundException, SystemException, ServiceConditionException, IOException {
        // Content-type validation

        Map<String, Object> response = execute(servletRequest, serviceName, operationName);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /************************ PUT *********************************/

    /************************ PATCH *********************************/
    /**
     * Used for all the Version API's
     *
     * @param servletRequest - servlet request
     * @param serviceName    - module name/ action class bean name
     * @param versionNumber  - version number for the corresponding API
     * @return response format
     */
    @RequestMapping(value = "/api/{service}/{operation}/{version}",
                    produces = {Http.XML_MIME, Http.JSON_MIME},
                    method = RequestMethod.PATCH)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> executeVersionApiWithPatch(HttpServletRequest servletRequest,
                                                    @PathVariable("service") String serviceName,
                                                    @PathVariable("operation") String operationName,
                                                    @PathVariable("version") String versionNumber)
            throws BadRequestException, NotFoundException, SystemException, ServiceConditionException, IOException {
        Map<String, Object> response = execute(servletRequest, serviceName, operationName);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Used for all the Version API's
     *
     * @param servletRequest - servlet request
     * @param serviceName    - module name/ action class bean name
     * @return response format
     */
    @RequestMapping(value = "/api/{service}/{operation}",
                    produces = {Http.XML_MIME, Http.JSON_MIME},
                    method = RequestMethod.PATCH)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> executePassThroughApiWithPatch(HttpServletRequest servletRequest,
                                                    @PathVariable("service") String serviceName,
                                                    @PathVariable("operation") String operationName)
            throws BadRequestException, NotFoundException, SystemException, ServiceConditionException, IOException {
        Map<String, Object> response = execute(servletRequest, serviceName, operationName);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /************************ PATCH *********************************/

    /************************ DELETE *********************************/
    /**
     * Used for all the Version API's
     *
     * @param servletRequest - servlet request
     * @param serviceName    - module name/ action class bean name
     * @param versionNumber  - version number for the corresponding API
     * @return response format
     */
    @RequestMapping(value = "/api/{service}/{operation}/{version}",
                    produces = {Http.XML_MIME, Http.JSON_MIME},
                    method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> executeVersionApiWithDelete(HttpServletRequest servletRequest,
                                                    @PathVariable("service") String serviceName,
                                                    @PathVariable("operation") String operationName,
                                                    @PathVariable("version") String versionNumber)
            throws BadRequestException, NotFoundException, SystemException, ServiceConditionException, IOException {
        Map<String, Object> response = execute(servletRequest, serviceName, operationName);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Used for all the Version API's
     *
     * @param servletRequest - servlet request
     * @param serviceName    - module name/ action class bean name
     * @return response format
     */
    @RequestMapping(value = "/api/{service}/{operation}",
                    produces = {Http.XML_MIME, Http.JSON_MIME},
                    method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> executePassThroughApiWithDelete(HttpServletRequest servletRequest,
                                                    @PathVariable("service") String serviceName,
                                                    @PathVariable("operation") String operationName)
            throws BadRequestException, NotFoundException, SystemException, ServiceConditionException, IOException{
        Map<String, Object> response = execute(servletRequest, serviceName, operationName);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /************************ DELETE *********************************/

    private Map<String, Object> execute(HttpServletRequest httpServletRequest, String serviceName, String operationName)
            throws BadRequestException, NotFoundException, SystemException, ServiceConditionException, IOException {
        checkContentType(httpServletRequest);

        // Make data map which contains API information, service, operation information and requestMap
        Map<String, Object> requestMap = readRequestFromServlet(httpServletRequest);

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("service", serviceName);
        dataMap.put("operation", operationName);
        dataMap.put("request", requestMap);
        if (logEnabled) logger.info(httpServletRequest.getRequestURI() + " request: " + requestMap);
        responseLogger.log(Http.SC_OK, httpServletRequest);
        // execute service
        return (Map<String, Object>) apiExecuteService.execute(httpServletRequest, dataMap);
    }
}
