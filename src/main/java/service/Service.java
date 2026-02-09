package service;

import controllers.UserController;
import http.Dispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Service {
    private static final Logger logger = LoggerFactory.getLogger(Service.class);
    public void execute(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("🚀 Server running in port " + port);

            Dispatcher dispatcher = new Dispatcher();

            RouteScanner.scan(new UserController(), dispatcher);

            ExecutorService threadPool = Executors.newFixedThreadPool(50);

            while (!serverSocket.isClosed()) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler handler = new ClientHandler(clientSocket, dispatcher);
                threadPool.execute(handler);
            }
        } catch (IOException e) {
            logger.error("Service Error: {}",  e.getMessage());
        }
    }
}
