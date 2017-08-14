package info.doula.service.impl;

import info.doula.exception.BadRequestException;
import info.doula.exception.NotFoundException;
import info.doula.exception.ServiceConditionException;
import info.doula.exception.SystemException;
import info.doula.logic.ApiExecuteLogic;
import info.doula.service.ApiExecuteService;
import info.doula.system.ConfReader;
import info.doula.system.MaintenanceChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import static info.doula.util.AppConstants.*;


/**
 * ApiExecuteService Used for executing all version and passThrough API's
 *
 * @author hossaindoula
 *
 */
@Component("apiExecuteService")
class ApiExecuteServiceImpl implements ApiExecuteService {

	private Logger logger = LoggerFactory.getLogger(ApiExecuteServiceImpl.class);
	private Logger executeLogger = LoggerFactory.getLogger("CALLAPI");
	private Logger executeErrorLogger = LoggerFactory.getLogger("CALLAPI_ERROR");
	private Logger errorLogger = LoggerFactory.getLogger("ERROR");
	private Logger slowLogger = LoggerFactory.getLogger("SLOW");
	private Logger conditionErrorLogger = LoggerFactory.getLogger("CONDITION_ERROR");

	@Autowired
    ConfReader confReader;

	@Autowired
    ApiExecuteLogic apiExecuteLogic;

	@Autowired
    MaintenanceChecker maintenanceChecker;

	private boolean logEnabled;

	/**
	 * Execute version API's
	 *
	 * @param servletRequest - httpServlet request
	 * @param dataMap - contains requestURI, serviceName, operationName, request
	 * @return responseMap
	 * @throws BadRequestException
	 * @throws NotFoundException
	 * @throws SystemException
	 * @throws ServiceConditionException
	 */
	@Override
	public LinkedHashMap<String, ?> execute(HttpServletRequest servletRequest, Map<String, Object> dataMap)
			throws BadRequestException, NotFoundException, SystemException, ServiceConditionException {
		long accessTime = System.currentTimeMillis();
		long time = 0;
		String logMsg = "";
		String requestBody = dataMap.get("request").toString();

		try {
			//Check maintenance status
			maintenanceChecker.checkApiMaintenanceStatus(dataMap.get("service").toString(),
					dataMap.get("operation").toString(), dataMap.get("version").toString());

			// Call API execute logic
			LinkedHashMap<String,?> result = apiExecuteLogic.executeService(servletRequest, dataMap);

			// Write into logs
			time = System.currentTimeMillis() - accessTime;
			logMsg = "SUCCESS	[" + servletRequest.getRequestURI()+ "]	" + time;
			executeLogger.info(logMsg);
			slowLog(logMsg, time);

			return result;
		} catch (BadRequestException e) {
			wrapExceptionThrow(time, accessTime,
					servletRequest, e, BAD_REQUEST, logMsg, requestBody);
			throw e;
		} catch (NotFoundException e) {
			wrapExceptionThrow(time, accessTime,
					servletRequest, e, NOT_FOUND, logMsg, requestBody);
			throw e;
		} catch (ServiceConditionException e) {
			wrapExceptionThrow(time, accessTime,
					servletRequest, e, SERVICE_UNAVAILABLE, logMsg, requestBody);
			throw e;
		} catch (SystemException e) {
			wrapExceptionThrow(time, accessTime,
					servletRequest, e, SYSTEM_ERROR, logMsg, requestBody);
			throw e;
		} catch (Exception e) {
			wrapExceptionThrow(time, accessTime,
					servletRequest, e, SYSTEM_ERROR, logMsg, requestBody);
			throw new SystemException("Fatal Exception");
		}
	}

	/**
	 * Execute passThrough API's
	 *
	 * @param servletRequest - httpServlet request
	 * @param dataMap - contains requestURI, serviceName, operationName, request
	 * @return responseMap
	 * @throws BadRequestException
	 * @throws NotFoundException
	 * @throws SystemException
	 * @throws ServiceConditionException
	 */
	@Override
	public LinkedHashMap<String, ?> passThrough(HttpServletRequest servletRequest, Map<String, Object> dataMap)
			throws SystemException, NotFoundException, ServiceConditionException, BadRequestException {

		long accessTime = System.currentTimeMillis();
		long time = 0;
		String logMsg = "";
		String requestBody = dataMap.get("request").toString();

		try {

			//Check maintenance status
			maintenanceChecker.checkApiMaintenanceStatus(dataMap.get("service").toString(),
					dataMap.get("operation").toString());

			// Call API execute logic
			LinkedHashMap<String,?> result = apiExecuteLogic.executeService(servletRequest, dataMap);

			// Write into logs
			time = System.currentTimeMillis() - accessTime;
			logMsg = "SUCCESS	[" + servletRequest.getRequestURI() + "]	" + time;
			executeLogger.info(logMsg);
			slowLog(logMsg, time);

			return result;
		} catch (BadRequestException e) {
			wrapExceptionThrow(time, accessTime,
					servletRequest, e, BAD_REQUEST, logMsg, requestBody);
			throw e;
		} catch (NotFoundException e) {
			wrapExceptionThrow(time, accessTime,
					servletRequest, e, NOT_FOUND, logMsg, requestBody);
			throw e;
		} catch (ServiceConditionException e) {
			wrapExceptionThrow(time, accessTime,
					servletRequest, e, SERVICE_UNAVAILABLE, logMsg, requestBody);
			throw e;
		} catch (SystemException e) {
			wrapExceptionThrow(time, accessTime,
					servletRequest, e, SYSTEM_ERROR, logMsg, requestBody);
			throw e;
		} catch (Exception e) {
			wrapExceptionThrow(time, accessTime,
					servletRequest, e, SYSTEM_ERROR, logMsg, requestBody);
			throw new SystemException("Fatal Exception");
		}
	}

	private String esc(String original) {
		if (original == null)
			return "";
		else
			return original.replaceAll("\r", "").
					replaceAll("\n", "").replaceAll("\t", "");
	}

	private void slowLog(String logMsg, long time) {
		long slow = Long.valueOf(confReader.getProperty("platform.slowLog.time", "2000"));

		if (slow <= time) {
			slowLogger.error("[ApiExecute]\t" + logMsg);
		}
	}

	private void wrapExceptionThrow(long time, long accessTime, HttpServletRequest servletRequest,
									Exception e, String exceptionType, String logMsg, String requestBody) {
		time = System.currentTimeMillis() - accessTime;
		logMsg = exceptionType + "	[" + servletRequest.getRequestURI() + "]	" +
				esc(e.getMessage()) + " " + requestBody+ " " + time;
		executeErrorLogger.error(logMsg);
		errorLogger.error(e.getMessage(), e);
		slowLog(logMsg, time);
	}
}
