package com.org.ibpts.errorhandling;

public class ApiException  extends Exception {

    public  ApiException(Exception e) {
        super(e);
    }

    protected  ApiException() {
        super();
    }

    public  ApiException(String message) {
        super(message);
    }

    public ApiException(String message, Throwable cause) {
        super(message, cause);
    }

    protected  ApiException(Throwable cause) {
        super(cause);
    }
}
