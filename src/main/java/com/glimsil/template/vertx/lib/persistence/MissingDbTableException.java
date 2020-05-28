package com.glimsil.template.vertx.lib.persistence;

public class MissingDbTableException extends RuntimeException {
    public MissingDbTableException(String message) {
        super(message);
    }
}
