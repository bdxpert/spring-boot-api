package info.doula.exception;

import org.springframework.http.HttpStatus;

/**
 * Created by hossaindoula<hossain.doula@itconquest.com> on 2017-04-19.
 */
public class TimeoutException extends ApiException {

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

    public HttpStatus getServiceStatus(){
        return HttpStatus.REQUEST_TIMEOUT;
    }
}
