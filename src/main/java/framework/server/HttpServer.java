package framework.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {
    private static final Logger logger = LoggerFactory.getLogger(HttpServer.class);

    public void start(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            logger.info("Single-Threaded Server running in port {}", port);

            while (!serverSocket.isClosed()) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler handler = new ClientHandler(clientSocket);
                handler.run();
            }
        } catch (IOException e) {
            logger.error("Server Error: {}", e.getMessage());
        }
    }
}