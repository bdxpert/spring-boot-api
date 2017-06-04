package info.doula.system;


import info.doula.exception.ServiceConditionException;

/**
 * Maintenance check for all passThrough and version API's
 * Can check either service or service.operation or service.operation.version is under maintenance
 *
 * @author hossaindoula
 */
public interface MaintenanceChecker {

	/**
	 * Maintenance check for passThrough API's
	 * @param service
	 * @param operationName
	 * @throws ServiceConditionException
	 */
	void checkApiMaintenanceStatus(String service, String operationName) throws ServiceConditionException;

	/**
	 * Maintenance check for version API's
	 * @param serviceName
	 * @param operationName
	 * @param versionName
	 * @throws ServiceConditionException
	 */
	void checkApiMaintenanceStatus(String serviceName, String operationName, String versionName) throws ServiceConditionException;

	/**
	 * check maintenance of Affiliate LinkID System
	 *
	 * @throws ServiceConditionException
	 */
	void checkLinkIDMaint() throws ServiceConditionException;

}
