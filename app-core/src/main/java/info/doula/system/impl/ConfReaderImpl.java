package info.doula.system.impl;

import info.doula.annotation.AppEngineMethod;
import info.doula.exception.SystemException;
import info.doula.system.ConfReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.util.Properties;

/**
 * Read platform configuration properties
 *
 * @author hossaindoula
 *
 */
@Component
public class ConfReaderImpl implements ConfReader {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final Logger errorLogger = LoggerFactory.getLogger("ERROR");


	private String configurationPath;

	private Properties prop = null;

	/**
	 * Get property
	 * @param key
	 * @return value of the property
	 */
	@Override
	public String getProperty(String key) {
		return prop.getProperty(key);
	}

	/**
	 * Get property, if property not found default will be returned
	 * @param key
	 * @param defaultValue
	 * @return value of the property
	 */
	@Override
	public String getProperty(String key, String defaultValue) {
		String val = this.getProperty(key);

		if (val == null) {
			return defaultValue;
		}

		return val;
	}

	/**
	 * Check whether the value is true or not for the given property
	 * @param key
	 * @return
	 */
	@Override
	public boolean isTrue(String key) {
		return "true".equals(getProperty(key));
	}

	/**
	 * Set property configuration file path
	 * @param configurationPath
	 */
	public void setConfigurationPath(String configurationPath) {
		this.configurationPath = configurationPath;
	}

	/**
	 * Loads the properties form the given path
	 */
	@AppEngineMethod
	@PostConstruct
	public void load() throws SystemException {
		try {
			Properties temp = new Properties();
			temp.load(new FileInputStream(configurationPath));
			prop = temp;
			logger.info("dynamic properties loaded : " + prop.toString());
		} catch (Exception e) {
			errorLogger.error("Read conf error.", e);
			throw new SystemException("load.conf.error", e);
		}
	}
}
