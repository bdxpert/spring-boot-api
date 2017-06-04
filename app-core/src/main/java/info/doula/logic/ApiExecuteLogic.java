package info.doula.logic;

import info.doula.exception.BadRequestException;
import info.doula.exception.NotFoundException;
import info.doula.exception.ServiceConditionException;
import info.doula.exception.SystemException;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * Execute logic for the corresponding API
 * Business logic beans will initialized and get it from the bean factory class
 *
 * @author hossaindoula
 *
 */
public interface ApiExecuteLogic {

	/**
	 * Provides business logic object and execute for the given request 
	 * 
	 * @param servletRequest - http servlet request
	 * @param dataMap - contains serviceName, operationName, versionNumber, requestMap
	 * @return response 
	 * @throws BadRequestException
	 * @throws NotFoundException
	 * @throws SystemException
	 * @throws ServiceConditionException
	 */
	LinkedHashMap<String, ?> executeService(HttpServletRequest servletRequest, Map<String, Object> dataMap) throws BadRequestException, NotFoundException, SystemException, ServiceConditionException;

}
