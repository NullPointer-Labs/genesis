package service;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Service {
    public void execute(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server running in port " + port);

            Dispatcher dispatcher = registerControllers();
            ExecutorService theadPool = Executors.newFixedThreadPool(50);

            while (!serverSocket.isClosed()) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New Connection: " + clientSocket.getInetAddress());

                ClientHandler handler = new ClientHandler(clientSocket, dispatcher);
                theadPool.execute(handler);
            }
        } catch (IOException e) {
            System.out.println("Fatal server error: " + e.getMessage());
        }
    }

    private static Dispatcher registerControllers() {
        Dispatcher dispatcher = new Dispatcher();
        dispatcher.register("/api", (req, res) -> {
            res.setStatusCode(200);
            res.setBody("{\"status\": \"api working\"}");
            res.setContentType("application/json");
        });

        dispatcher.register("/api/user", (req, res) -> {
            if (req.getMethod().equals("GET")) {
                res.setStatusCode(200);
                res.setBody("{\"id\": 1, \"name\": \"Wesley\", \"role\": \"Java Developer\"}");
                res.setContentType("application/json");

            } else if (req.getMethod().equals("POST")) {
                String body = req.getBody();
                res.setStatusCode(201);
                res.setBody("{\"message\": \"User created\", \"data\": " + body + "}");
                res.setContentType("application/json");
            }
        });

        dispatcher.register("/api/search", (req, res) -> {
            String query = req.getParam("q");

            if (query == null) {
                res.setStatusCode(400);
                res.setBody("{\"error\": \"Missing query param 'q'\"}");
                res.setContentType("application/json");
                return;
            }

            res.setStatusCode(200);
            res.setBody("{\"result\": \"Searching for: " + query + "\"}");
            res.setContentType("application/json");
        });
        return dispatcher;
    }
}
