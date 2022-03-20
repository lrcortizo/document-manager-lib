package com.lrcortizo.document.manager.exception;

public class DocumentManagerException extends RuntimeException {

    public DocumentManagerException(final String msg) {
        super(msg);
    }

    public DocumentManagerException(final Throwable cause) {
        super(cause);
    }
}
