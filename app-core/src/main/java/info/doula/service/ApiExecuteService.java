package info.doula.service;

import info.doula.exception.BadRequestException;
import info.doula.exception.NotFoundException;
import info.doula.exception.ServiceConditionException;
import info.doula.exception.SystemException;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 * ApiExecuteService Used for executing all version and passThrough API's
 *
 * @author hossaindoula
 *
 */
public interface ApiExecuteService {

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
	LinkedHashMap<String, ?> execute(HttpServletRequest servletRequest, Map<String, Object> dataMap)
			throws BadRequestException, NotFoundException,
			SystemException, ServiceConditionException;

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
	LinkedHashMap<String, ?> passThrough(HttpServletRequest servletRequest, Map<String, Object> dataMap)
			throws BadRequestException, NotFoundException, SystemException, ServiceConditionException;

}
