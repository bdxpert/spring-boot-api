package jp.co.rakuten.ichiba.platform.system.impl

import java.util.LinkedHashMap;

import javax.annotation.PostConstruct

import jp.co.rakuten.ichiba.platform.annotation.AppEngineMethod
import jp.co.rakuten.ichiba.platform.system.ApiRepository
import jp.co.rakuten.ichiba.platform.exception.*

import java.util.LinkedHashMap
import groovy.io.FileType
import groovy.json.JsonSlurper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Api repository 
 * 
 * @author Sekhar Akkisetti <ts-sekhar.akkisetti@rakuten.com>
 */
@Component("apiRepository")
class ApiRepositoryImpl  implements ApiRepository {

	private final Logger logger = LoggerFactory.getLogger(this.class)
	
	private String configurationPath

	def allApiTemplates
	
	public void setConfigurationPath(String configurationPath) {
		this.configurationPath = configurationPath
	}
	
	/**
	 * Get template for specific Api
	 * @param apiKey
	 * @param requestUri
	 * @return templateMap
	 */
	@Override
	public LinkedHashMap<String, ?> getApiTemplateParameterMap(String apiKey, String requestUri) {
		return this.getApiTemplateParameterMap(apiKey, null, requestUri)
	}
	
	

	/**
	 * Get template for specific Api
	 * @param operation
	 * @param version
	 * @param requestUri
	 * @return
	 */
	@Override
	public LinkedHashMap<String, ?> getApiTemplateParameterMap(String operation, String version, String requestUri) {
		
		String apiKey = version ? "${operation}/${version}" : operation
		def apiTemplateParameterMap = allApiTemplates[apiKey]

		if(apiTemplateParameterMap == null) {

			def defaultApiTemplateMap = null
			String apiPath = "${operation}/"
			defaultApiTemplateMap = allApiTemplates.find { k, v ->
				k.toString().contains(apiPath)
			}
			if(defaultApiTemplateMap) {
				throw new IchibaWrongParameterException("Invalid API version")
			} else {
				throw new IchibaWrongParameterException("${requestUri} api doesnot exist")
			}
		}
		return apiTemplateParameterMap
	}
	
	@AppEngineMethod
	@PostConstruct
	def load() {
		
		long startTime = System.currentTimeMillis()
		def tempAPITemplateMap = [:]
		File apiDir = new File(configurationPath)
		
		//Service directories
		apiDir.eachDir {
			String serviceName = it.name
			
			//Passthrough API's and Version API's file list
			it.eachFile(FileType.FILES) {
			
				(it.name =~ /^([0-9a-zA-Z_\.-]+)\.json/).each { fullName, fileName ->
					
					File apiFile = new File(it as String)
					try {
						String apiFileName = fileName.toString().replaceFirst("\\.", "/")
						def jsonTemplateMap = (LinkedHashMap)(new JsonSlurper().parseText(apiFile.getText()))
						tempAPITemplateMap[serviceName + "/" + apiFileName] = jsonTemplateMap
					} catch(Exception ex) {
						throw new IchibaSystemException("Api file loading error ${apiFile}", ex)
					}
				}
			}
		}
		
		allApiTemplates = tempAPITemplateMap
		long endTime = System.currentTimeMillis()

		logger.info("ApiRepository is loaded. it takes ${endTime - startTime}ms")
	}
	
	/**
	 * Get all Api templates
	 * @return
	 */
	@Override
	public LinkedHashMap<String, ?> getAllApiTemplates() {
		return allApiTemplates
	}
	
}
