package org.euaggelion.theauthenticapp.Exception;


public class OtpDeliveryException extends RuntimeException {
    public OtpDeliveryException(String message) {
        super(message);
    }

    public OtpDeliveryException(String message, Throwable cause) {
        super(message, cause);
    }
}
