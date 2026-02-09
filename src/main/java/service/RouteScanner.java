package service;

import annotations.DELETE;
import annotations.GET;
import annotations.POST;
import annotations.PUT;
import http.Dispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

public class RouteScanner {
    private static final Logger logger = LoggerFactory.getLogger(RouteScanner.class);

    public static void scan(Object controller, Dispatcher dispatcher) {
        Class<?> clazz = controller.getClass();

        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(GET.class)) {
                GET annotation = method.getAnnotation(GET.class);
                dispatcher.register("GET", annotation.value(), (req, res) -> {
                    try {
                        method.invoke(controller, req, res);
                    } catch (Exception e) {
                        logger.error("GET Error: {}", e.getMessage());
                        res.setStatusCode(500);
                    }
                });
            } else if (method.isAnnotationPresent(POST.class)) {
                POST annotation = method.getAnnotation(POST.class);
                dispatcher.register("POST", annotation.value(), (req, res) -> {
                    try {
                        method.invoke(controller, req, res);
                    } catch (Exception e) {
                        logger.error("POST Error: {}", e.getMessage());
                        res.setStatusCode(500);
                    }
                });
            } else if (method.isAnnotationPresent(PUT.class)) {
                PUT annotation = method.getAnnotation(PUT.class);
                dispatcher.register("PUT", annotation.value(), (req, res) -> {
                    try {
                        method.invoke(controller, req, res);
                    } catch (Exception e) {
                        logger.error("PUT Error: {}", e.getMessage());
                        res.setStatusCode(500);
                    }
                });
            } else if (method.isAnnotationPresent(DELETE.class)) {
                DELETE annotation = method.getAnnotation(DELETE.class);
                dispatcher.register("DELETE", annotation.value(), (req, res) -> {
                    try {
                        method.invoke(controller, req, res);
                    } catch (Exception e) {
                        logger.error("DELETE Error: {}", e.getMessage());
                        res.setStatusCode(500);
                    }
                });
            }
        }
    }
}