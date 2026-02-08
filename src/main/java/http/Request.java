package http;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Request {
    private String method;
    private String path;
    private String version;
    private String body;
    private final Map<String, String> queryParams = new HashMap<>();

    public Request(BufferedReader reader) throws IOException {
        String firstLine = reader.readLine();
        if (firstLine == null || firstLine.isEmpty()) return;

        String[] parts = firstLine.split(" ");

        if (parts.length >= 3) {
            this.method = parts[0];
            String fullPath = parts[1];
            this.version = parts[2];

            if (fullPath.contains("?")) {
                String[] pathParts = fullPath.split("\\?", 2);
                this.path = pathParts[0];
                String queryString = pathParts[1];
                parseQueryParams(queryString);
            } else {
                this.path = fullPath;
            }
        }

        String line;
        Map<String, String> headers = new HashMap<>();
        while ((line = reader.readLine()) != null && !line.isEmpty()) {
            String[] headerParts = line.split(":", 2);
            if (headerParts.length == 2) {
                headers.put(headerParts[0].trim(), headerParts[1].trim());
            }
        }

        if (headers.containsKey("Content-Length")) {
            int contentLength = Integer.parseInt(headers.get("Content-Length"));
            char[] bodyChars = new char[contentLength];

            reader.read(bodyChars, 0, contentLength);
            this.body = new String(bodyChars);
        }
    }

    private void parseQueryParams(String queryString) {
        String[] pairs = queryString.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=", 2);
            if (keyValue.length == 2) {
                String key = keyValue[0];
                String value = URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8);
                queryParams.put(key, value);
            }
        }
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getVersion() {
        return version;
    }

    public String getBody() {
        return body;
    }

    public String getParam(String key) {
        return queryParams.get(key);
    }

    public boolean isValid() {
        return method != null && !method.isEmpty();
    }
}
