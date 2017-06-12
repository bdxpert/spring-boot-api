package info.doula.util;

import java.math.BigDecimal;

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
        } catch(NumberFormatException | NullPointerException e) {
            return 0;
        }
    }

    public static long toLong(Object number){
        try{
            return Long.parseLong(number.toString());
        } catch(NumberFormatException | NullPointerException e) {
            return 0;
        }
    }

    public static boolean compareGreaterThan(Object firstNumber, Object secondNumber){
        if(isInteger(firstNumber.toString()) && isInteger(secondNumber.toString())){
            return toInt(firstNumber) > toInt(secondNumber);
        } else if(isLong(firstNumber.toString()) && isLong(secondNumber.toString())){
            return toLong(firstNumber) > toLong(secondNumber);
        }
        return false;
    }

    public static boolean compareLessThan(Object firstNumber, Object secondNumber){
        if(isInteger(firstNumber.toString()) && isInteger(secondNumber.toString())){
            return toInt(firstNumber) < toInt(secondNumber);
        } else if(isLong(firstNumber.toString()) && isLong(secondNumber.toString())){
            return toLong(firstNumber) < toLong(secondNumber);
        }
        return false;
    }

    public static boolean isNumber(String number){
        return org.apache.commons.lang3.math.NumberUtils.isNumber(number);
    }

    public static boolean isBigDecimal(String number){
        return org.apache.commons.lang3.math.NumberUtils.createBigDecimal(number) instanceof BigDecimal;
    }
}
