package info.doula.logic.impl;

import info.doula.exception.ServiceConditionException;
import info.doula.logic.CallApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Call API using GET, POST, POST_JSON, POST_XML
 *
 * @author
 *
 */
@Component
class CallApiLogicImpl implements CallApi {

	@Value(value = "${module.callapi.log}")
	private boolean logEnabled;

	@Override
	public String requestGet(Map<String, ?> connectionMap, Map<String, ?> param)
	throws ServiceConditionException {
		return "";
	}

	@Override
	public String requestPost(Map<String, ?> connectionMap, Map<String, ?> param)
	throws ServiceConditionException {
		return "";
	}

	@Override
	public String requestPostBody(Map<String, ?> connectionMap, String requestBody, String contentType)
	throws ServiceConditionException {
		return "";
	}

	@Override
	public String requestSynchronousGet(Map<String, ?> connectionMap, Map<String, ?> param) throws ServiceConditionException, IOException {
		return null;
	}

	@Override
	public String requestAsynchronousGet(Map<String, ?> connectionMap, Map<String, ?> param) throws ServiceConditionException, IOException {
		return null;
	}

	@Override
	public String requestPostBody(Map<String, ?> connectionMap) throws ServiceConditionException, IOException, Exception {
		return null;
	}

	@Override
	public String requestPostStreaming(Map<String, ?> connectionMap) throws ServiceConditionException, IOException, Exception {
		return null;
	}

	@Override
	public String requestPostFile(Map<String, ?> connectionMap) throws ServiceConditionException, IOException, Exception {
		return null;
	}

	@Override
	public String requestPostForm(Map<String, ?> connectionMap) throws ServiceConditionException, IOException, Exception {
		return null;
	}

	@Override
	public String requestPostMultipart(Map<String, ?> connectionMap, File file, String mimeType) throws ServiceConditionException, IOException, Exception {
		return null;
	}
}
