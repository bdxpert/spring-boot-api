package info.doula;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Date Util
 *
 * @author hossaindoula<hossaindoula@gmail.com>
 *
 */
public class DateUtil {

	/**
	 * format ms to
	 *
	 * @param ms
	 * @param format
	 * @return
	 */
	public static String format(long ms, String format) {
		return format(new Date(ms), format);
	}

	public static String format(Date date, String format) {
		if (date == null || isEmpty(format))
			return null;
		return new SimpleDateFormat(format).format(date);
	}

	public static long toEpocSecond(long ms) {
		return ms / 1000L;
	}

	public static long toEpocSecond(Date date) {
		if (date == null)
			return 0;

		return toEpocSecond(date.getTime());
	}

	public static Date convertToDate(long s, boolean isEpoc) {
		return new Date(isEpoc ? s * 1000L : s);
	}

	public static Date convertToDate(String dateString, String format) {
		return convertToDate(dateString, format, null);
	}

	public static Date convertToDate(String dateString, String format,
			Date defaultValue) {
		if (dateString == null || format == null)
			return defaultValue;

		try {
			return new SimpleDateFormat(format).parse(dateString);
		} catch (ParseException e) {
			return defaultValue;
		}
	}

	public static long getBeforeCalenderDate(int month) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, month);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		return cal.getTime().getTime() / 1000;
	}

	public static long convertPertinentDate(long epocDate) {
		return (epocDate - ((epocDate + TimeZone.getDefault().getRawOffset()) % (24 * 60 * 60 * 1000))) / 1000;
	}

	public static final boolean isEmpty(String s) {
		return (s == null || s.trim().length() == 0);
	}

	public static Date getToday(int day) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DATE, cal.get(Calendar.DATE) + day);
		return cal.getTime();
	}

}
