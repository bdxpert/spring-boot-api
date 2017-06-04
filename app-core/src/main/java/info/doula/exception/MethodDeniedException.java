package info.doula.exception;

/**
 * Created by hossaindoula<hossain.doula@itconquest.com> on 2017-04-19.
 */
public class MethodDeniedException extends Exception {

    /**
     * @param message
     */
    public MethodDeniedException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public MethodDeniedException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public MethodDeniedException(String message, Throwable cause) {
        super(message, cause);
    }
}
