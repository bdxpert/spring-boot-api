package info.doula.service;

import java.util.Map;

/**
 * Convert given map to xml string
 *
 * @author hossaindoula <hossaindoula@gmail.com>
 *
 */
public interface XmlConvertService {

	/**
	 * Convert given request to xml string
	 * @param data
	 * @param slimMode
	 * @return xmlString
	 */
	public String convert(Map<String, ?> data, boolean slimMode);
}
