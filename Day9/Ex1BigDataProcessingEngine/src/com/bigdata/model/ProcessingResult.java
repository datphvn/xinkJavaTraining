package com.bigdata.model;

public class ProcessingResult<T> {
    private final T data;
    public ProcessingResult(T data) { this.data = data; }
    public T getData() { return data; }
}
