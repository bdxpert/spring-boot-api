package info.doula.service.impl;

import info.doula.exception.SystemException;
import info.doula.service.ConfigurationService;
import info.doula.system.ConfReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Configuration service to check service in or out
 *
 * @author hossaindoula <hossaindoula@gmail.com>
 *
 */
@Component("configurationService")
public class ConfigurationServiceImpl implements ConfigurationService {

	@Autowired
    ConfReader confReader;

	@Value(value = "${environment}")
	private String environment;

	/**
	 * Provides the current environment
	 *
	 * @return environment
	 */
	@Override
	public String getEnvironment() {
		return environment;
	}

	/**
	 * Check current service status
	 *
	 * @return current service status
	 */
	@Override
	public boolean isServiceIn() {
		return "true".equals(confReader.getProperty("service.in"));
	}

	/**
	 * Reload dynamic properties for updating service in & out status
	 */
	@Override
	public void reloadServiceStatus() throws SystemException {
		confReader.load();
	}
}
