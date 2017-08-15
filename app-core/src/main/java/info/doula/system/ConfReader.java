package info.doula.system;

import info.doula.exception.SystemException;

/**
 * Read platform configuration properties
 *
 * @author hossaindoula
 *
 */
public interface ConfReader {

	/**
	 * Get property
	 * @param key
	 * @return value of the property
	 */
	String getProperty(String key);

	/**
	 * Get property, if property not found default will be returned
	 * @param key
	 * @param defaultValue
	 * @return value of the property
	 */
	String getProperty(String key, String defaultValue);

	/**
	 * Check whether the value is true or not for the given property
	 * @param key
	 * @return
	 */
	boolean isTrue(String key);

	/**
	 * Load service properties
	 */
	void load() throws SystemException;

}
