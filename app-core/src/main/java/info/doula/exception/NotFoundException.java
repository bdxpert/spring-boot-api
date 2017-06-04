package info.doula.exception;

/**
 * Created by hossaindoula<hossain.doula@itconquest.com> on 2017-04-19.
 */
public class NotFoundException extends Exception {

    /**
     * @param message
     */
    public NotFoundException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public NotFoundException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}