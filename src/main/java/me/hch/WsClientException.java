package me.hch;

/**
 * Created by Administrator on 14-4-12.
 */
public class WsClientException extends RuntimeException {
    public WsClientException() {
        super();
    }

    public WsClientException(String message) {
        super(message);
    }

    public WsClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public WsClientException(Throwable cause) {
        super(cause);
    }
}
