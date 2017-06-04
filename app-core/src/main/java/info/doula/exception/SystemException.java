package info.doula.exception;

/**
 * Created by hossaindoula<hossain.doula@itconquest.com> on 2017-04-19.
 */
public class SystemException extends Exception {

    private boolean needToAlert = true;

    /**
     * @param message
     */
    public SystemException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public SystemException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public SystemException(String message, Throwable cause) {
        super(message, cause);
    }

    public boolean isNeedToAlert() {
        return needToAlert;
    }

    public void setNeedToAlert(boolean needToAlert) {
        this.needToAlert = needToAlert;
    }

}
