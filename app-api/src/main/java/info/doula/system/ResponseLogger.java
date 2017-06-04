package info.doula.system;

import javax.servlet.http.HttpServletRequest;

import info.doula.response.Http;
import info.doula.util.AppConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.fileupload.FileItem;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

public class ResponseLogger {
	private final Logger responseLogger = LoggerFactory.getLogger("RESPONSE");
	private final Logger errorLogger = LoggerFactory.getLogger("ERROR");

	private List<?> multipartItems = null;

	public void setMultipartItems(List<?> items) {
		multipartItems = items;
	}

	public void log(int status, HttpServletRequest request) {
		try {
			List<String> list = new ArrayList<String>();
			list.add(request.getMethod());
			list.add(request.getRequestURL().toString());

			String contentType = request.getContentType();

			list.add(String.valueOf(status));

			if(contentType != null && contentType.contains(Http.FORM_URL_ENCODED)){
				// request parameters
				@SuppressWarnings("rawtypes")
				Enumeration params = request.getParameterNames();
				String param = "";
				while (params.hasMoreElements()) {
					String keyString = (String) params.nextElement();
					String valueString = request.getParameter(keyString);
					if(keyString.equals("password"))
						valueString = "XXXXXXXX";

					if (param.length() > 0)
						param += "&";

					param += keyString + "=" + valueString;
				}
				list.add(param);
			} else if(contentType != null && contentType.contains(Http.MULTIPART_FORM)) {
				String param = "";
                if(multipartItems != null && multipartItems.size() > 0) {
	                Iterator<?> iterator = multipartItems.iterator();
	                while (iterator.hasNext()) {
	                	String keyString;
						String valueString;

	                    FileItem item = (FileItem) iterator.next();
	                    keyString = item.getFieldName();
	                    if (!item.isFormField()) {
	                    	valueString = item.getName();
	                    } else if (keyString.equals("password")) {
	                    	valueString = "XXXXXXXX";
	                    } else {
	                    	valueString = item.getString();
	                    }

	                    if (param.length() > 0) {
							param += "&";
						}
						param += keyString + "=" + valueString;
	                }
                }
                list.add(param);
			} else{
				// Retreiving the body content from the HttpservletRequest.Attribute
				if (null != request.getAttribute(Http.BODY_CONTENT)
							&& request.getAttribute(Http.BODY_CONTENT).toString().length() > 0) {
					// Adding to the Response log list.
					list.add(request.getAttribute(Http.BODY_CONTENT).toString());
				}
			}

			// request headers
			@SuppressWarnings("rawtypes")
			Enumeration headerEnumeration = request.getHeaderNames();
			String header = "";
			while (headerEnumeration.hasMoreElements()) {
				String headerName = (String) headerEnumeration.nextElement();
				String headerValue = request.getHeader(headerName);
				if (header.length() > 0) {
					header += "&";
				}
				header += headerName + "=" + headerValue;
			}

			list.add(header);

			String forwarded = request.getHeader(Http.FORWARD_FOR);
			if(forwarded != null){
				list.add("forwarded");
				list.add(forwarded);
			} else {
				list.add("not-forwarded");
				list.add(request.getRemoteAddr());
			}

			String logMessage = "";
			for (String str : list) {
				if(str == null)
					str = "";
				if (logMessage.length() > 0)
					logMessage += "\t";
				str.replaceAll("\r", "").replaceAll("\n", "").replaceAll("\t", "");
				logMessage += str;
			}

			responseLogger.info(logMessage);
		} catch (Exception e) {
			errorLogger.error(AppConstants.RESP_LOG_ERR,e);
		}
	}
}
