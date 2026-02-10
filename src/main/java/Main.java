import application.controller.UserController;
import application.exceptions.BadRequestException;
import application.exceptions.ResourceNotFoundException;
import application.repository.UserRepository;
import application.repository.impl.UserRepositoryImpl;
import application.service.UserService;
import com.google.gson.Gson;
import framework.http.GlobalExceptionHandler;
import framework.server.HttpServer;

import java.util.Map;

public class Main {
    public static void main(String[] args) {
        int port = 8080;
        HttpServer server = new HttpServer();
        Gson gson = new Gson();

        GlobalExceptionHandler.getInstance().register(ResourceNotFoundException.class, (e, res) -> {
            res.setStatusCode(404);
            res.setBody(gson.toJson(Map.of("error", "Not Found", "message", e.getMessage())));
            res.setContentType("application/json");
        });

        GlobalExceptionHandler.getInstance().register(BadRequestException.class, (e, res) -> {
            res.setStatusCode(400);
            res.setBody(gson.toJson(Map.of("error", "Bad Request", "message", e.getMessage())));
            res.setContentType("application/json");
        });

        UserRepository repository = new UserRepositoryImpl();
        UserService userService = new UserService(repository);
        UserController userController = new UserController(userService);
        server.registerController(userController);
        server.start(port);
    }
}