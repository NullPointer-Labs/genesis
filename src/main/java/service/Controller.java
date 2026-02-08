package service;

import http.Request;
import http.Response;

public interface Controller {
    void handle(Request request, Response response);
}