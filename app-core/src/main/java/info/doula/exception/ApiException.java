package info.doula.exception;

import org.springframework.http.HttpStatus;

/**
 * Created by hossaindoula on 6/15/2017.
 */
public abstract class ApiException extends Exception {

    private String extraMessage;


    /**
     * @param message
     */
    public ApiException(String message) {
        super(message);
    }

    public ApiException(String message, Object extraMessage){
        super(message);
        this.extraMessage = extraMessage.toString();
    }

    /**
     * @param cause
     */
    public ApiException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public ApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getExtraMessage(){
        return this.extraMessage;
    }

    public abstract HttpStatus getServiceStatus();

    public String getServiceMessage(){
        return "";
    }
}