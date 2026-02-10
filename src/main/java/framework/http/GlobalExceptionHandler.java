package framework.http;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private static final GlobalExceptionHandler INSTANCE = new GlobalExceptionHandler();
    private final Map<Class<? extends Throwable>, ErrorHandler> handlers = new HashMap<>();
    private final Gson gson = new Gson();

    public interface ErrorHandler {
        void handle(Throwable e, Response res);
    }

    public static GlobalExceptionHandler getInstance() {
        return INSTANCE;
    }

    public void register(Class<? extends Throwable> exceptionClass, ErrorHandler handler) {
        handlers.put(exceptionClass, handler);
    }

    public void handle(Throwable e, Response res) {
        ErrorHandler handler = handlers.get(e.getClass());

        if (handler != null) {
            handler.handle(e, res);
        } else {
            res.setStatusCode(500);
            res.setContentType("application/json");
            res.setBody(gson.toJson(Map.of("error", "Internal Server Error", "details", e.getMessage())));
            logger.error("Internal Server Error", e);
        }
    }
}
