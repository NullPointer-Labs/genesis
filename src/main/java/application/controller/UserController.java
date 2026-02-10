package application.controller;

import application.db.Database;
import application.model.User;
import framework.http.Request;
import framework.http.Response;

import java.util.List;

public class UserController {

    public void listUsers(Request req, Response res) {
        List<User> users = Database.findAll();

        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < users.size(); i++) {
            User u = users.get(i);
            json.append(String.format("{\"id\":%d,\"name\":\"%s\",\"email\":\"%s\"}",
                    u.getId(), u.getName(), u.getEmail()));
            if (i < users.size() - 1) json.append(",");
        }
        json.append("]");

        res.setBody(json.toString());
        res.setStatusCode(200);
        res.setContentType("application/json");
    }

    public void getUser(Request req, Response res) {
        String idParam = req.getParam("id");
        if (idParam == null) {
            res.setStatusCode(400);
            res.setBody("{\"error\": \"ID required\"}");
            return;
        }

        try {
            int id = Integer.parseInt(idParam);
            User user = Database.findById(id);

            if (user == null) {
                res.setStatusCode(404);
                res.setBody("{\"error\": \"User not found\"}");
                return;
            }

            String json = String.format("{\"id\": %d, \"name\": \"%s\", \"email\": \"%s\"}",
                    user.getId(), user.getName(), user.getEmail());

            res.setBody(json);
            res.setStatusCode(200);
            res.setContentType("application/json");

        } catch (NumberFormatException e) {
            res.setStatusCode(400);
            res.setBody("{\"error\": \"ID must be a number\"}");
        }
    }

    public void createUser(Request req, Response res) {
        String body = req.getBody();
        try {
            String name = extractJsonValue(body, "name");
            String email = extractJsonValue(body, "email");

            if (name == null || name.length() < 3) {
                res.setStatusCode(400);
                res.setBody("{\"error\": \"Name too short\"}");
                return;
            }

            User newUser = new User(0, name, email);
            User saved = Database.save(newUser);

            String json = String.format(
                    "{\"id\": %d, \"name\": \"%s\", \"email\": \"%s\"}",
                    saved.getId(), saved.getName(), saved.getEmail()
            );

            res.setStatusCode(201);
            res.setBody(json);
            res.setContentType("application/json");

        } catch (Exception e) {
            res.setStatusCode(500);
            res.setBody("{\"error\": \"Failed to parse JSON\"}");
        }
    }

    public void deleteUser(Request req, Response res) {
        String idParam = req.getParam("id");
        if (idParam == null) {
            res.setStatusCode(400);
            res.setBody("{\"error\": \"ID required\"}");
            return;
        }

        try {
            int id = Integer.parseInt(idParam);
            boolean deleted = Database.delete(id);

            if (deleted) {
                res.setStatusCode(204);
            } else {
                res.setStatusCode(404);
                res.setBody("{\"error\": \"User not found\"}");
                res.setContentType("application/json");
            }
        } catch (NumberFormatException e) {
            res.setStatusCode(400);
            res.setBody("{\"error\": \"ID must be a number\"}");
        }
    }

    private String extractJsonValue(String json, String key) {
        String search = "\"" + key + "\":";
        int start = json.indexOf(search);
        if (start == -1) return null;

        start += search.length();
        while (json.charAt(start) == ' ' || json.charAt(start) == '"') start++;

        int end = json.indexOf("\"", start);
        return json.substring(start, end);
    }
}