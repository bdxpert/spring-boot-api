package info.doula.exception;

/**
 * Created by hossaindoula<hossain.doula@itconquest.com> on 2017-04-19.
 */
public class AuthorizationException extends Exception {

    /**
     * @param message
     */
    public AuthorizationException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public AuthorizationException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public AuthorizationException(String message, Throwable cause) {
        super(message, cause);
    }
}