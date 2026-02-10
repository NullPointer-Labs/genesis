package framework.http;

import java.util.HashMap;
import java.util.Map;

public class Router {
    private final Map<String, HttpHandler> routes = new HashMap<>();

    public void register(String method, String path, HttpHandler httpHandler) {
        String key = method.toUpperCase() + ":" + path;
        routes.put(key, httpHandler);
    }

    public boolean dispatch(Request request, Response response) {
        String key = request.getMethod().toUpperCase() + ":" + request.getPath();

        HttpHandler httpHandler = routes.get(key);

        if (httpHandler != null) {
            httpHandler.handle(request, response);
            return true;
        }
        return false;
    }

}
