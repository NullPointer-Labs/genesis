import application.controller.UserController;
import application.repository.UserRepository;
import application.repository.impl.UserRepositoryImpl;
import application.service.UserService;
import framework.server.HttpServer;

public class Main {
    public static void main(String[] args) {
        int port = 8080;
        HttpServer server = new HttpServer();

        UserRepository repository = new UserRepositoryImpl();
        UserService userService = new UserService(repository);
        UserController userController = new UserController(userService);
        server.registerController(userController);
        server.start(port);
    }
}