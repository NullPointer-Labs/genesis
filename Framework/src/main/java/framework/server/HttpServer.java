package framework.server;

import framework.annotations.RestController;
import framework.di.DIContainer;
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
    private final DIContainer diContainer = new DIContainer();

    public void initialize(String basePackage) {
        try {
            diContainer.init(basePackage);

            for (Object bean : diContainer.getAllBeans()) {
                if (bean.getClass().isAnnotationPresent(RestController.class)) {
                    RouteScanner.scan(bean, router);
                }
            }
        } catch (Exception e) {
            logger.error("Failed to initialize framework", e);
            throw new RuntimeException(e);
        }
    }

    public void start(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server running in port " + port);

            ExecutorService threadPool = Executors.newFixedThreadPool(50);

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