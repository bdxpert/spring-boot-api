package info.doula.system;

import info.doula.exception.BadRequestException;

import java.util.LinkedHashMap;

/**
 * Api repository
 *
 * @author hossaindoula<hossaindoula@gmail.com>
 */
public interface ApiRepository {

	/**
	 * Get template for specific Api
	 * @param apiKey
	 * @param requestUri
	 * @return templateMap
	 */
	LinkedHashMap<String, ?> getApiTemplateParameterMap(String apiKey, String requestUri) throws BadRequestException;

	/**
	 * Get template for specific Api
	 * @param operation
	 * @param version
	 * @param requestUri
	 * @return
	 */
	LinkedHashMap<String, ?> getApiTemplateParameterMap(String operation, String version, String requestUri) throws BadRequestException;

	/**
	 * Get all Api templates
	 * @return
	 */
	LinkedHashMap<String, ?> getAllApiTemplates();
}
