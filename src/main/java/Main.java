import application.controller.UserController;
import framework.server.HttpServer;

public class Main {
    public static void main(String[] args) {
        int port = 8080;
        HttpServer server = new HttpServer();
        server.registerController(new UserController());
        server.start(port);
    }
}