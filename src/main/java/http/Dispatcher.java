package http;

import service.Controller;

import java.util.HashMap;
import java.util.Map;

public class Dispatcher {
    private final Map<String, Controller> routes = new HashMap<>();

    public void register(String method, String path, Controller controller) {
        String key = method.toUpperCase() + ":" + path;
        routes.put(key, controller);
    }

    public boolean dispatch(Request request, Response response) {
        String key = request.getMethod().toUpperCase() + ":" + request.getPath();

        Controller controller = routes.get(key);

        if (controller != null) {
            controller.handle(request, response);
            return true;
        }
        return false;
    }

}
