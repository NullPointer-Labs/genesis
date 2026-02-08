package service;

import http.Request;
import http.Response;

import java.util.HashMap;
import java.util.Map;

public class Dispatcher {
    private final Map<String, Controller> routes = new HashMap<>();

    public void register(String path, Controller controller) {
        routes.put(path, controller);
    }

    public boolean dispatch(Request request, Response response) {
        Controller controller = routes.get(request.getPath());
        if (controller != null) {
            controller.handle(request, response);
            return true;
        }
        return false;
    }

}
