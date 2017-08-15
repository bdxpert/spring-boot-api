package info.doula.system.impl;

import info.doula.exception.ServiceConditionException;
import info.doula.system.ConfReader;
import info.doula.system.MaintenanceChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Maintenance check for all passThrough and version API's
 * Can check either service or service.operation or service.operation.version is under maintenance
 *
 * @author hossaindoula
 */
@Component
public class MaintenanceCheckerImpl implements MaintenanceChecker {

	@Autowired
	private ConfReader confReader;

	/**
	 * Maintenance check for passThrough API's
	 * @param serviceName
	 * @param operationName
	 * @throws ServiceConditionException
	 */
	@Override
	public void checkApiMaintenanceStatus(String serviceName, String operationName)
			throws ServiceConditionException {

		// Check complete service is under maintenance
		if (confReader.isTrue(serviceName + ".maintenance")) {
			throw new ServiceConditionException(serviceName + " service is under maintenance");
		}

		// Check operation is under maintenance
		if (confReader.isTrue(serviceName + "." + operationName + ".maintenance")) {
			throw new ServiceConditionException(serviceName + "/" + operationName + " is under maintenance");
		}
	}

	/**
	 * Maintenance check for version API's
	 * @param serviceName
	 * @param operationName
	 * @param versionName
	 * @throws ServiceConditionException
	 */
	@Override
	public void checkApiMaintenanceStatus(String serviceName, String operationName, String versionName)
			throws ServiceConditionException {

		// Check service or operation is under maintenance
		checkApiMaintenanceStatus(serviceName, operationName);

		if (confReader.isTrue(serviceName + "." + operationName + "." + versionName + ".maintenance")) {
			throw new ServiceConditionException(serviceName + "/" + operationName + "/" + versionName +" is under maintenance");
		}
	}

	/**
	 * check maintenance of Affiliate LinkID System
	 *
	 * @throws ServiceConditionException
	 */
	@Override
	public void checkLinkIDMaint() throws ServiceConditionException {
		if (confReader.isTrue("linkdb.maintenance")) {
			throw new ServiceConditionException("aff.lnk.maint");
		}
	}
}
