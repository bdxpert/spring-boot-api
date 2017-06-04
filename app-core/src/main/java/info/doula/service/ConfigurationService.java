package info.doula.service;

/**
 * Configuration service to check service in or out
 *
 * @author hossaindoula <hossaindoula@gmail.com>
 *
 */
public interface ConfigurationService {

	/**
	 * Provides the current environment
	 *
	 * @return environment
	 */
	String getEnvironment();

	/**
	 * Check current service status
	 *
	 * @return current service status
	 */
	boolean isServiceIn();

	/**
	 * Reload dynamic properties for updating service in & out status
	 */
	public void reloadServiceStatus();

}
