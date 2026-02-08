package http;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class Response {
    private int statusCode;
    private byte[] body;
    private String contentType;

    public void send(OutputStream os) {
        byte[] actualBody = (body == null) ? new byte[0] : body;
        try {
            os.write(("HTTP/1.1 " + statusCode + " " + getReasonPhrase(statusCode) + "\r\n").getBytes());
            os.write(("Content-Type: " + contentType + "\r\n").getBytes());
            os.write(("Content-Length: " + actualBody.length + "\r\n").getBytes());
            os.write("\r\n".getBytes());
            os.write(actualBody);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }
    public void setBody(String body) {
        this.body = body.getBytes(StandardCharsets.UTF_8);
    }
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    private String getReasonPhrase(int code) {
        return switch (code) {
            case 200 -> "OK";
            case 201 -> "Created";
            case 202 -> "Accepted";
            case 204 -> "No Content";
            case 400 -> "Bad http.Request";
            case 401 -> "Unauthorized";
            case 403 -> "Forbidden";
            case 404 -> "Not Found";
            default -> "Internal Server Error";
        };
    }
}
