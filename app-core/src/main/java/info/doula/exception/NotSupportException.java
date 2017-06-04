package info.doula.exception;

/**
 * Created by hossaindoula<hossain.doula@itconquest.com> on 2017-04-19.
 */
public class NotSupportException extends Exception {

    /**
     * @param message
     */
    public NotSupportException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public NotSupportException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public NotSupportException(String message, Throwable cause) {
        super(message, cause);
    }
}