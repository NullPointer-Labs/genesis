import application.controller.UserController;
import framework.server.HttpServer;

public class Main {
    public static void main(String[] args) {
        HttpServer server = new HttpServer();
        UserController userController = new UserController();

        server.get("/api/users", userController::listUsers);
        server.start(8080);
    }
}