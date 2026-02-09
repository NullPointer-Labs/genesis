package service;

import http.Dispatcher;
import http.Request;
import http.Response;
import util.ServerUtils;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Socket socket;
    private final Dispatcher dispatcher;

    public ClientHandler(Socket socket, Dispatcher dispatcher) {
        this.socket = socket;
        this.dispatcher = dispatcher;
    }

    @Override
    public void run() {
        try (
            socket;
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            OutputStream writer = socket.getOutputStream()
        ) {
            Request request = new Request(reader);
            if (!request.isValid()) return;
            Response response = new Response();
            boolean handled = dispatcher.dispatch(request, response);

            if (!handled) {
                serveStaticFile(request, response);
            }
            response.send(writer);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void serveStaticFile(Request request, Response response) {
        String path = request.getPath();
        if (path.equals("/")) {
            path = "/index.html";
        }

        InputStream fileStream = getClass().getResourceAsStream(path);

        if (fileStream != null) {
            try {
                byte[] fileBytes = fileStream.readAllBytes();
                response.setStatusCode(200);
                response.setBody(fileBytes);
                response.setContentType(ServerUtils.guessContentType(path));
            } catch (IOException e) {
                response.setStatusCode(500);
                response.setBody("Error read file");
            }
        } else {
            response.setStatusCode(404);
            response.setBody("<h1>404 - Not Found</h1>");
            response.setContentType("text/html");
        }
    }
}