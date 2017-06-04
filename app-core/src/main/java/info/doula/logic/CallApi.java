package info.doula.logic;

import info.doula.exception.ServiceConditionException;

import java.io.File;
import java.io.IOException;
import java.util.Map;


/**
 * Call API using GET, POST, POST_JSON, POST_XML
 * 
 * @author hossaindoula
 *
 */
public interface CallApi {

	String requestGet(Map<String, ?> connectionMap, Map<String, ?> param) throws ServiceConditionException;

	String requestPost(Map<String, ?> connectionMap, Map<String, ?> param) throws ServiceConditionException;

	String requestPostBody(Map<String, ?> connectionMap, String jsonBody, String contentType) throws ServiceConditionException;

	String requestSynchronousGet(Map<String, ?> connectionMap, Map<String, ?> param) throws ServiceConditionException, IOException;

	String requestAsynchronousGet(Map<String, ?> connectionMap, Map<String, ?> param) throws ServiceConditionException, IOException;

	String requestPostBody(Map<String, ?> connectionMap) throws ServiceConditionException, IOException, Exception;

	String requestPostStreaming(Map<String, ?> connectionMap) throws ServiceConditionException, IOException, Exception;

	String requestPostFile(Map<String, ?> connectionMap) throws ServiceConditionException, IOException, Exception;

	String requestPostForm(Map<String, ?> connectionMap) throws ServiceConditionException, IOException, Exception;

	String requestPostMultipart(Map<String, ?> connectionMap, File file, String mimeType) throws ServiceConditionException, IOException, Exception;

}
