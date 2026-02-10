package framework.http;

public interface HttpHandler {
    void handle(Request request, Response response);
}