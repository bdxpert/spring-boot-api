package info.doula.logic.impl;

import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import info.doula.exception.BadRequestException;
import info.doula.exception.NotFoundException;
import info.doula.exception.ServiceConditionException;
import info.doula.exception.SystemException;
import info.doula.logic.ApiExecuteLogic;
import info.doula.logic.ApiReflectionLogic;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * Execute logic for the corresponding API
 * Business logic beans will initialized and get it from the bean factory class
 *
 * @author hossaindoula
 *
 */
@Component
public class ApiExecuteLogicImpl implements ApiExecuteLogic, BeanFactoryAware {

	private BeanFactory beanFactory;

	@Override
	public void setBeanFactory(BeanFactory beanFactory) {
		this.beanFactory = beanFactory;
	}

	@Autowired
	ApiReflectionLogic apiReflectionLogic;

	/**
	 * Provides business logic object and execute for the given request
	 *
	 * @param servletRequest - http servlet request
	 * @param dataMap - contains serviceName, operationName, versionNumber, requestMap
	 * @return response
	 * @throws SystemException
	 * @throws NotFoundException
	 * @throws BadRequestException
	 * @throws ServiceConditionException
	 */
	@SuppressWarnings("unchecked")
	@Override
	public LinkedHashMap<String, ?> executeService(HttpServletRequest servletRequest, Map<String, Object> dataMap)
			throws BadRequestException, NotFoundException,
			SystemException, ServiceConditionException {

		// Call API execute logic
		Object logicObject;

		try {
			logicObject = beanFactory.getBean(dataMap.get("service").toString());
			if(logicObject != null)
				throw new BadRequestException(servletRequest.getRequestURI() + "api.not.exist");
		} catch(BadRequestException ex) {
			throw ex;
		} catch(Exception ex) {
			throw new SystemException("not.found.obj" + servletRequest.getRequestURI() + " : " + ex.getMessage(), ex);
		}

		// Call reflection logic to invoke the main action class of module
		return (LinkedHashMap<String, ?>)apiReflectionLogic.execute(servletRequest, dataMap, logicObject);
	}
}
