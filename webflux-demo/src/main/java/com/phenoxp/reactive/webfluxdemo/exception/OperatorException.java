package com.phenoxp.reactive.webfluxdemo.exception;

public class OperatorException extends RuntimeException {

    private static final long serialVersionUID = 7214527322414363424L;

    private static final String MSG = "is not allowed";
    public static final int ERROR_CODE = 200;
    private final String input;

    public OperatorException(String input) {
        super(MSG);
        this.input = input;
    }

    public String getInput() {
        return input;
    }
}
