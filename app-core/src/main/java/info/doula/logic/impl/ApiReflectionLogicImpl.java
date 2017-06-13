package info.doula.logic.impl;


import info.doula.exception.BadRequestException;
import info.doula.exception.NotFoundException;
import info.doula.exception.ServiceConditionException;
import info.doula.exception.SystemException;
import info.doula.logic.ApiReflectionLogic;
import info.doula.util.AppConstants;
import info.doula.annotation.*;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXParseException;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Reflection logic for calling respective module logic object by passing required annotaion instances from core
 *
 * @author hossaindoula
 *
 */
@Component
public class ApiReflectionLogicImpl implements ApiReflectionLogic {

	@Autowired
	BeanFactory beanFactory;

	@Value(value = "${environment}")
	private String environment;

	@Value(value = "${region}")
	private String region;

	@Value(value = "${domain}")
	private String domain;

	/**
	 * execute and call module business logic object
	 *
	 * @param servletRequest - http servlet request
	 * @param dataMap - contains requestURI, serviceName, operationName, versionNumber, requestMap
	 * @return response
	 * @throws SystemException
	 * @throws NotFoundException
	 * @throws BadRequestException
	 * @throws ServiceConditionException
	 */
	@Override
	public Object execute(HttpServletRequest servletRequest, Map<String, Object> dataMap, Object logicObject)
			throws SystemException, NotFoundException,
			BadRequestException, ServiceConditionException {
		try {

			Map<String,Object> requestParameter = (Map<String,Object>)dataMap.get("request");
			String methodName = dataMap.get("operation").toString();

			for (Method method : logicObject.getClass().getMethods()) {

				AppEngineMethod methodAnnotation = method
						.getAnnotation(AppEngineMethod.class);

				if (methodAnnotation != null
				&& methodAnnotation.value().equals(methodName)) {
					List<Object> invokeParameter = new ArrayList<>();

					Annotation[][] parameterAnnotations = method
							.getParameterAnnotations();

					for (Annotation[] annotations : parameterAnnotations) {
						if (annotations.length > 0) {
							Annotation annotation = annotations[0];
							if (annotation instanceof Parameter) {
								Parameter p = (Parameter) annotation;
								if (requestParameter.containsKey(p.value())) {
									invokeParameter.add(requestParameter.get(p
											.value()));
								} else {
									invokeParameter.add(null);
								}
							} else if (annotation instanceof ParameterMap) {
								invokeParameter.add(requestParameter);
							} else if (annotation instanceof FormParameterMap) {
								invokeParameter.add(requestParameter);
							} else if (annotation instanceof HeaderParameterMap) {
								Map<String, String> headerMap = getHeadersFromRequest(servletRequest);
								invokeParameter.add(headerMap);
							} else if (annotation instanceof Environment) {
								invokeParameter.add(environment);
							} else if (annotation instanceof Operation) {
								invokeParameter.add(dataMap.get("operation"));
							} else if (annotation instanceof Version) {
								invokeParameter.add(dataMap.get("version"));
							} else if (annotation instanceof RequestURI) {
								invokeParameter.add(servletRequest.getRequestURI());
							} else if (annotation instanceof Region) {
								invokeParameter.add(region);
							} else if (annotation instanceof Domain) {
								invokeParameter.add(domain);
							} else if (annotation instanceof Bean) {
								Bean b = (Bean) annotation;
								String beanName = b.value();
								invokeParameter.add(beanFactory.getBean(beanName, beanFactory.getType(beanName)));
							} else if (annotation instanceof RemoteAddress) {
								invokeParameter.add(servletRequest.getRemoteAddr());
							} else {
								invokeParameter.add(null);
							}
						} else {
							invokeParameter.add(null);
						}
					}

					return method.invoke(logicObject, invokeParameter.toArray());
				}
			}
		} catch (NumberFormatException e) {
			throw new SystemException(AppConstants.NUM_F_ERR, e);

		} catch (SecurityException e) {
			throw new SystemException(AppConstants.SEC_ERR, e);

		} catch (IllegalArgumentException e) {
			throw new SystemException(AppConstants.ILL_ARG_ERR, e);

		} catch (IllegalAccessException e) {
			throw new SystemException(AppConstants.ILL_ACC_ERR, e);

		} catch (InvocationTargetException e) {
			Throwable cause = e.getCause();

			if (cause instanceof SAXParseException) {
				throw new ServiceConditionException(AppConstants.ILL_DAT_PAR_ERR, cause);

			} else if (cause instanceof NotFoundException) {
				throw (NotFoundException) cause;

			} else if (cause instanceof BadRequestException) {
				throw (BadRequestException) cause;

			} else if (cause instanceof ServiceConditionException) {
				throw (ServiceConditionException) cause;

			} else if (cause instanceof SystemException) {
				throw (SystemException) cause;
			} else {
				throw new SystemException("api.logic.error" + " : " +e.getMessage(), e.getCause());
			}
		}

		throw new BadRequestException(servletRequest.getRequestURI() + " " + "api.not.exist");
	}

	/**
	 * Get Headers from servlet request
	 * @param servletRequest
	 * @return headerMap
	 */
	private Map<String, String> getHeadersFromRequest(HttpServletRequest servletRequest) {

		Map<String, String> headerMap = new HashMap<String, String>();
		Enumeration headerNames = servletRequest.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String key = (String) headerNames.nextElement();
			String value = servletRequest.getHeader(key);
			headerMap.put(key, value);
		}

		return headerMap;
	}
}
