package framework.service;

import framework.annotations.*;
import framework.http.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

public class RouteScanner {
    private static final Logger logger = LoggerFactory.getLogger(RouteScanner.class);

    public static void scan(Object controller, Router router) {
        Class<?> clazz = controller.getClass();

        boolean isRestController = clazz.isAnnotationPresent(RestController.class);

        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(GET.class)) {
                registerRoute(router, "GET", method.getAnnotation(GET.class).value(), controller, method, isRestController);
            } else if (method.isAnnotationPresent(POST.class)) {
                registerRoute(router, "POST", method.getAnnotation(POST.class).value(), controller, method, isRestController);
            } else if (method.isAnnotationPresent(PUT.class)) {
                registerRoute(router, "PUT", method.getAnnotation(PUT.class).value(), controller, method, isRestController);
            } else if (method.isAnnotationPresent(DELETE.class)) {
                registerRoute(router, "DELETE", method.getAnnotation(DELETE.class).value(), controller, method, isRestController);
            }
        }
    }
    private static void registerRoute(Router router, String verb, String path, Object controller, Method method, boolean isJson) {
        router.register(verb, path, (req, res) -> {
            try {
                Object result;
                if (method.getParameterCount() == 0) {
                    result = method.invoke(controller);
                } else {
                    result = method.invoke(controller, req, res);
                }

                if (result != null) {
                    String responseBody = result.toString();

                    res.setBody(responseBody);
                    res.setContentType("application/json");

                    if (res.getStatusCode() == 0) {
                        res.setStatusCode(200);
                    }
                }
            } catch (Exception e) {
                logger.error("Internal Server Error: {}", e.getMessage());
                res.setStatusCode(500);
                res.setBody("{\"error\": \"Internal Server Error\"}");
            }
        });
        logger.info("Mapped {} {} -> {}", verb, path, method.getName());
    }
}