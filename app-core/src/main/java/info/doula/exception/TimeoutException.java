package info.doula.exception;

/**
 * Created by hossaindoula<hossain.doula@itconquest.com> on 2017-04-19.
 */
public class TimeoutException extends Exception {

    /**
     * @param message
     */
    public TimeoutException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public TimeoutException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public TimeoutException(String message, Throwable cause) {
        super(message, cause);
    }
}