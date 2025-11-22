package com.xink.training.day13.service;

public class HttpResponse {
    private final int statusCode;
    private final String message;
    private String body;
    
    public HttpResponse(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
    
    public int getStatusCode() {
        return statusCode;
    }
    
    public String getMessage() {
        return message;
    }
    
    public String getBody() {
        return body;
    }
    
    public void setBody(String body) {
        this.body = body;
    }
}

