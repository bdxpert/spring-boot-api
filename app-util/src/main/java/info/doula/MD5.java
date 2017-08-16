package info.doula;

import java.security.MessageDigest;

/**
 * Converts to MD5 sum
 * @author hossaindoula<hossaindoula@itconquest.com>
 */
public class MD5 {
	public static String md5(String str) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] dat = str.getBytes();
			md.update(dat);
			return toHex(md.digest());
		} catch (Exception e) {
			return null;
		}
	}

	private static String toHex(byte[] digest) {
		StringBuffer buff = new StringBuffer();
		for(int i = 0; i < digest.length; i++) {
			int d = digest[i];
			if (d < 0) {
				d += 256;
			}
			if (d < 16) {
				buff.append("0");
			}
			buff.append(Integer.toString(d, 16));
		}
		return buff.toString();
	}
}
