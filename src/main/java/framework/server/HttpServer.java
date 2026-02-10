package framework.server;

import framework.http.HttpHandler;
import framework.http.Router;
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

    public void addRoute(String method, String path, HttpHandler handler) {
        router.register(method, path, handler);
    }

    public void get(String path, HttpHandler handler) {
        addRoute("GET", path, handler);
    }

    public void post(String path, HttpHandler handler) {
        addRoute("POST", path, handler);
    }

    public void delete(String path, HttpHandler handler) {
        addRoute("DELETE", path, handler);
    }

    public void start(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            logger.info("Server running in port {}", port);
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