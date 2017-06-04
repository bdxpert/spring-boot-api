package info.doula.logic;

import info.doula.exception.BadRequestException;
import info.doula.exception.NotFoundException;
import info.doula.exception.ServiceConditionException;
import info.doula.exception.SystemException;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 * Reflection logic for calling respective module logic object by passing required annotation instances from core 
 * 
 * @author hossaindoula <hossaindoula@gmail.com>
 *
 */
public interface ApiReflectionLogic {

	/**
	 * execute and call module business logic object
	 * 
	 * @param servletRequest - http servlet request
	 * @param dataMap - contains serviceName, operationName, versionNumber, requestMap
	 * @param beanClass - business logic object
	 * @return response
	 * @throws BadRequestException
	 * @throws NotFoundException
	 * @throws SystemException
	 * @throws ServiceConditionException
	 */
	Object execute(HttpServletRequest servletRequest, Map<String, Object> dataMap, Object beanClass)
			throws BadRequestException, NotFoundException,
			SystemException, ServiceConditionException;

}
