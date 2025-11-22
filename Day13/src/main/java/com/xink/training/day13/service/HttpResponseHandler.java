package com.xink.training.day13.service;

public class HttpResponseHandler {
    
    public String getMessage(HttpResponse response) {
        return response.getMessage();
    }
    
    public boolean isError(HttpResponse response) {
        return response.getStatusCode() >= 400;
    }
    
    public boolean isValidStatus(int statusCode) {
        return statusCode >= 100 && statusCode < 600;
    }
}

