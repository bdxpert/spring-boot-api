package info.doula.util;

/**
 * Created by tasnim on 6/11/2017.
 */
public class NumberUtils {

    public static boolean isInteger(String s) {
        return isInteger(s,10);
    }

    private static boolean isInteger(String s, int radix) {
        if(s.isEmpty()) return false;
        for(int i = 0; i < s.length(); i++) {
            if(i == 0 && s.charAt(i) == '-') {
                if(s.length() == 1) return false;
                else continue;
            }
            if(Character.digit(s.charAt(i),radix) < 0) return false;
        }
        return true;
    }

    public static boolean isLong(String s) {
        try {
            Long.parseLong(s);
        } catch(NumberFormatException e) {
            return false;
        } catch(NullPointerException e) {
            return false;
        }
        return true;
    }

    public static int toInt(Object number){
        try{
            return Integer.parseInt(number.toString());
        } catch(NumberFormatException e) {
        } catch(NullPointerException e) {
        }
        return 0;
    }
}
