package framework.server;

import framework.http.Request;
import framework.http.Response;
import framework.http.Router;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Socket socket;
    private final Router router;

    public ClientHandler(Socket socket, Router router) {
        this.socket = socket;
        this.router = router;
    }

    @Override
    public void run() {
        try (
                socket; BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                OutputStream writer = socket.getOutputStream()
        ) {
            Request request = new Request(reader);

            if (!request.isValid()) return;

            Response response = new Response();

            boolean handled = router.dispatch(request, response);

            if (!handled) {
                response.setStatusCode(404);
                response.setBody("{\"error\": \"Endpoint not found\"}");
                response.setContentType("application/json");
            }

            response.send(writer);

        } catch (Exception e) {
            System.err.println("Error handling client: " + e.getMessage());
        }
    }
}