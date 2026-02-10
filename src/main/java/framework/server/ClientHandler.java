package framework.server;

import framework.http.Request;
import framework.http.Response;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (
                socket;
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                OutputStream writer = socket.getOutputStream()
        ) {
            Thread.sleep(5000);
            Request request = new Request(reader);
            if (!request.isValid()) return;

            Response response = new Response();

            String path = request.getPath();

            if (path.equals("/")) {
                path = "/index.html";
            }

            InputStream fileStream = getClass().getResourceAsStream(path);

            if (fileStream != null) {
                byte[] fileBytes = fileStream.readAllBytes();

                response.setStatusCode(200);
                response.setContentType(guessContentType(path));
                response.setBody(fileBytes);

            } else {
                response.setStatusCode(404);
                response.setContentType("text/html");
                response.setBody("<h1>404 - Arquivo Nao Encontrado</h1>");
            }

            response.send(writer);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private String guessContentType(String path) {
        if (path.endsWith(".html")) return "text/html";
        if (path.endsWith(".css")) return "text/css";
        if (path.endsWith(".js")) return "application/javascript";
        if (path.endsWith(".png")) return "image/png";
        if (path.endsWith(".jpg")) return "image/jpeg";
        return "text/plain";
    }
}