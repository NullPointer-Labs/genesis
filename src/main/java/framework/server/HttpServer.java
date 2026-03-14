package framework.server;

import framework.http.Router;
import framework.service.RouteScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServer {
    private static final Logger logger = LoggerFactory.getLogger(HttpServer.class);
    private final Router router = new Router();


    public void registerController(Object controller) {
        RouteScanner.scan(controller, router);
    }

    public void start(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server running in port " + port);

            ExecutorService threadPool = Executors.newVirtualThreadPerTaskExecutor();

            while (!serverSocket.isClosed()) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler handler = new ClientHandler(clientSocket, router);
                threadPool.execute(handler);
            }
        } catch (IOException e) {
            logger.error("Server Error: {}", e.getMessage());
        }
    }
}