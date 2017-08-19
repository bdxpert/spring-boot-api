package info.doula.system.impl;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;

import javax.annotation.PostConstruct;

import java.util.Map;

import info.doula.annotation.AppEngineMethod;
import info.doula.exception.BadRequestException;
import info.doula.exception.SystemException;
import info.doula.system.ApiRepository;
import info.doula.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Api repository
 *
 * @author hossaindoula<hossaindoula@gmail.com>
 */
@Component("apiRepository")
class ApiRepositoryImpl  implements ApiRepository {

	private static final Logger logger = LoggerFactory.getLogger(ApiRepositoryImpl.class);

	@Value("${api.repository.path:defaultValue}")
	private String configurationPath;

	LinkedHashMap<String, Object> allApiTemplates;

	public void setConfigurationPath(String configurationPath) {
		this.configurationPath = configurationPath;
	}

	/**
	 * Get template for specific Api
	 * @param apiKey
	 * @param requestUri
	 * @return templateMap
	 */
	@Override
	public LinkedHashMap<String, Object> getApiTemplateParameterMap(String apiKey, String requestUri) throws BadRequestException {
		return getApiTemplateParameterMap(apiKey, null, requestUri);
	}

	/**
	 * Get template for specific Api
	 * @param operation
	 * @param version
	 * @param requestUri
	 * @return
	 */
	@Override
	public LinkedHashMap<String, Object> getApiTemplateParameterMap(String operation, String version, String requestUri) throws BadRequestException {

		String apiKey = version != null ? operation + "/" + version : operation;
		LinkedHashMap<String, Object> apiTemplateParameterMap = (LinkedHashMap<String, Object>) allApiTemplates.get(apiKey);

		if(apiTemplateParameterMap == null) {

			boolean defaultApiTemplateMap = false;
			String apiPath = operation + "/";

			Map<String, Object> allTemplates = allApiTemplates;

			for(Map.Entry<String,Object> entry : allTemplates.entrySet()) {
				defaultApiTemplateMap = entry.getKey().contains(apiPath);
			}

			if(defaultApiTemplateMap) {
				throw new BadRequestException("Invalid API version");
			} else {
				throw new BadRequestException(requestUri + " api doesnot exist");
			}
		}
		return apiTemplateParameterMap;
	}

	@AppEngineMethod
	@PostConstruct
	public void load() throws SystemException {
		long startTime = System.currentTimeMillis();
		LinkedHashMap<String, Object> tempAPITemplateMap = new LinkedHashMap<>();
		File apiDir = new File(configurationPath);
		for(String serviceName : apiDir.list()){

			File apiFileDir = new File(serviceName);
			for(String apiFile : apiFileDir.list())
			try {
				String apiFileName = apiFile.toString().replaceFirst("\\.", "/");

				try {
					tempAPITemplateMap.put(serviceName + "/" + apiFileName, FileUtil.readText(apiFileName));
				} catch(IOException ex){

				}
			} catch(Exception ex) {
				throw new SystemException("Api file loading error " + apiFile, ex);
			}
		}

		allApiTemplates = tempAPITemplateMap;
		long endTime = System.currentTimeMillis();

		logger.info("ApiRepository is loaded. it takes ", endTime - startTime);
	}

	/**
	 * Get all Api templates
	 * @return
	 */
	@Override
	public LinkedHashMap<String, Object> getAllApiTemplates() {
		return allApiTemplates;
	}

}