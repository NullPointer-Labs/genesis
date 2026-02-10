package framework.http;

import java.io.BufferedReader;
import java.io.IOException;

public class Request {
    private String method;
    private String path;

    public Request(BufferedReader reader) throws IOException {
        String firstLine = reader.readLine();
        if (firstLine == null || firstLine.isEmpty()) return;

        String[] parts = firstLine.split(" ");

        if (parts.length >= 2) {
            this.method = parts[0];
            String fullPath = parts[1];
            if (fullPath.contains("?")) {
                this.path = fullPath.split("\\?")[0];
            } else {
                this.path = fullPath;
            }
        }

    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public boolean isValid() {
        return method != null && !method.isEmpty();
    }
}