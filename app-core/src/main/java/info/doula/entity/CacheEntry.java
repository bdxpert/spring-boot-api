package info.doula.entity;


/**
 * author hossaindoula<hossaindoula@gmail.com>
 */
public class CacheEntry implements java.io.Serializable {

	private static final long serialVersionUID = 7378773359803909527L;

	private long time;
	private Object object;

	public CacheEntry(long time, Object object) {
		this.time = time;
		this.object = object;
	}

	public void clear() {
		time = 0;
		object = null;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}
}
