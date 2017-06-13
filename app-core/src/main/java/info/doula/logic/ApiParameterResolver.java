package info.doula.logic;

import info.doula.exception.ParameterResolveException;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * API Parameter resolver
 * Convert raw parameters to well-formed parameters
 *
 * @author Mohammed Hossain Doula <hossaindoula@gmail.com>
 *
 */
public interface ApiParameterResolver {

	/**
	 * Resolve request map
	 *
	 * If requestTemplateMap has fastforward is true then, it will return actual httpRequest
	 * It checks actual httpRequest with requestTemplateMap as mentioned json file and prepare the request accordingly
	 *
	 * @see info.doula.entity.JsonAttributes class for json attributes
	 *
	 * @param dataMap
	 * @param requestTemplateMap
	 * @return
	 */
	LinkedHashMap<String, ?> resolveRequestParameter(Map<String, Object> dataMap, Map<String, Object> requestTemplateMap)
			throws ParameterResolveException;


	/**
	 * Resolve response parameter
	 *
	 * If requestTemplateMap has fastforward is true then, it will return actual httpRequest
	 * It checks actual httpRequest with requestTemplateMap as mentioned json file and prepare the request accordingly
	 *
	 * @see info.doula.entity.JsonAttributes class for json attributes
	 *
	 * @param actualResponse
	 * @param responseTemplateMap
	 * @return
	 */
	LinkedHashMap<String, ?> resolveResponseParameter(Map<String, Object> actualResponse, Map<String, Object> responseTemplateMap)
			throws ParameterResolveException;

}
