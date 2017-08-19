package info.doula;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by hossaindoula on 4/15/2016.
 */
public class DateUtils {

    /**
     * 31-12-2011
     */
    public final static String DEFAULT_DATE_FORMAT = "dd-MM-yyyy";
    /**
     * 31/12/2011
     */
    public final static String DD_MM_YYYY = "dd/MM/yyyy";

    /**
     * 31-Dec-2011
     */
    public final static String DD_MMM_YYYY = "dd-MMM-yyyy";
    /**
     * 31/Dec/2011
     */
    public final static String DD__MMM_YYYY = "dd/MMM/yyyy";


    public static Date parseDate(Date date, String format){
        DateFormat dateFormat = new SimpleDateFormat(format);
        try{
            String dateStr = dateFormat.format(date);
            SimpleDateFormat formatter = new SimpleDateFormat(format);
            return formatter.parse(dateStr);
        } catch (ParseException pe){
            pe.printStackTrace();
            return null;
        }
    }

    public static Date parseDate(String date, String dateFormat){
        SimpleDateFormat fmt = new SimpleDateFormat(dateFormat);
        try {
            return fmt.parse(date);
        } catch (Exception e) {
            return null;
        }
    }

    public static String getYear(Date dateParams) {
        return parseDate(dateParams, "yyyy") != null ?
                parseDate(dateParams, "yyyy").toString().split(" ")[5] : "";
    }

    public static Date addDay(Date date, int numberOfDays) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, numberOfDays); //minus number would decrement the days
        return cal.getTime();
    }

    public static Date addDayWithCurrentSystemDate(int numberOfDays) {
        return addDay(new Date(), numberOfDays);
    }

    public static Date getAddedDateTime(Date date, int addedHour, int addedMinute) {
        date.setTime(date.getTime() + (((addedHour * 60 * 60 * 1000) + (addedMinute * 60 * 1000)) - 1));
        return date;
    }

    public static Date getAddedDateTime(Date date, int addedHour) {
        date.setTime(date.getTime() + ((addedHour * 60 * 60 * 1000) - 1));
        return date;
    }

    public static String getFormatDateByAddedDay(Date date, String dateFormat, int addedDay, String type) {
        Calendar cal = Calendar.getInstance();
        if (type.equals("DATE")) {
            cal.add(Calendar.DATE, addedDay);
        } else if (type.equals("MONTH")) {
            cal.add(Calendar.MONTH, addedDay);
        } else if (type.equals("YEAR")) {
            cal.add(Calendar.YEAR, addedDay);
        }
        SimpleDateFormat fmt = new SimpleDateFormat(dateFormat);
        return fmt.format(cal.getTime());
    }

    public static String getFormatDateByAddedDateTime(Date date, String dateFormat, int addedHour) {
        SimpleDateFormat fmt = new SimpleDateFormat(dateFormat);
        date.setTime(date.getTime() + ((addedHour * 60 * 60 * 1000) - 1));
        return fmt.format(date);
    }

    public static String getFormatDateByAddedDateTime(Date date, String dateFormat, int addedHour, int addedMinute) {
        SimpleDateFormat fmt = new SimpleDateFormat(dateFormat);
        date.setTime(date.getTime() + (((addedHour * 60 * 60 * 1000) + (addedMinute * 60 * 1000)) - 1));
        return fmt.format(date);
    }

    public static int getOffsetOfSystemTimeZone() {
        TimeZone tz = Calendar.getInstance().getTimeZone();
        return tz.getRawOffset();
    }

    public static int getMinuteOfSystemTimeZone() {
        int rawOffset = getOffsetOfSystemTimeZone();
        return Math.abs(rawOffset / (60 * 1000)) % 60;
    }

    public static int getHourOfSystemTimeZone() {
        int rawOffset = getOffsetOfSystemTimeZone();
        return rawOffset / (60 * 60 * 1000);
    }

    public static long getMillisecondByYear() {
        return getMillisecondByYear(1);
    }

    public static long getMillisecondByYear(int year) {
        long day = (1000 * 60 * 60 * 24); // 1 day in milliseconds
        long time = day * 365 * year; // for example, 1 year

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);
        return c.getTimeInMillis();
    }

    public static long getMillisecondByDay(int countDay) {
        long day = (1000 * 60 * 60 * 24); // 1 day in milliseconds
        long time = day * countDay; // for example, 2 days

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);
        return c.getTimeInMillis();
    }

    public static Date getGMTTime(Date dt) {
        return new Date(dt.getTime() - TimeZone.getDefault().getRawOffset());
    }
}
