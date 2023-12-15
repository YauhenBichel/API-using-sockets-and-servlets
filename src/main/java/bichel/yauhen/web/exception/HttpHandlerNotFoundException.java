package bichel.yauhen.web.exception;

/**
 * HttpHandlerNotFoundException Exception
 */
public class HttpHandlerNotFoundException extends RuntimeException {
    /**
     * Exception raising with error message and attached throwable exception
     * @param message String
     * @param ex Throwable
     */
    public HttpHandlerNotFoundException(String message, Throwable ex) {
        super(message, ex);
    }
}
