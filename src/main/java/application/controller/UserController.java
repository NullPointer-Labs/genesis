package application.controller;

import application.db.Database;
import application.model.User;
import framework.annotations.*;
import framework.http.Request;
import framework.http.Response;

import java.util.List;

@RestController
public class UserController {
    @GET("/api/users")
    public String listUsers() {
        List<User> users = Database.findAll();

        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < users.size(); i++) {
            User u = users.get(i);
            json.append("{")
                    .append("\"id\":").append(u.getId()).append(",")
                    .append("\"name\":\"").append(u.getName()).append("\",")
                    .append("\"email\":\"").append(u.getEmail()).append("\"")
                    .append("}");

            if (i < users.size() - 1) {
                json.append(",");
            }
        }
        json.append("]");
        return json.toString();
    }

    @GET("/api/users/detail")
    public String getUser(Request req, Response res) {
        String idParam = req.getParam("id");
        if (idParam == null) {
            res.setStatusCode(400);
            return "{\"error\": \"ID required\"}";
        }

        try {
            int id = Integer.parseInt(idParam);
            User user = Database.findById(id);

            if (user == null) {
                res.setStatusCode(404);
                return "{\"error\": \"User not found\"}";
            }

            return String.format(
                    "{\"id\": %d, \"name\": \"%s\", \"email\": \"%s\"}",
                    user.getId(), user.getName(), user.getEmail()
            );

        } catch (NumberFormatException e) {
            res.setStatusCode(400);
            return "{\"error\": \"ID must be a number\"}";
        }
    }

    @POST("/api/users")
    public String createUser(Request req, Response res) {
        String body = req.getBody();
        try {
            String name = extractJsonValue(body, "name");
            String email = extractJsonValue(body, "email");

            if (name == null || name.length() < 3) {
                res.setStatusCode(400);
                return "{\"error\": \"Name too short\"}";
            }

            User newUser = new User(0, name, email);
            User saved = Database.save(newUser);

            res.setStatusCode(201);
            return String.format(
                    "{\"id\": %d, \"name\": \"%s\", \"email\": \"%s\"}",
                    saved.getId(), saved.getName(), saved.getEmail()
            );
        } catch (Exception e) {
            res.setStatusCode(500);
            return "{\"error\": \"Failed to parse JSON\"}";
        }
    }

    @DELETE("/api/users")
    public String deleteUser(Request req, Response res) {
        String idParam = req.getParam("id");
        if (idParam == null) {
            res.setStatusCode(400);
            return "{\"error\": \"ID required\"}";
        }

        try {
            int id = Integer.parseInt(idParam);
            boolean deleted = Database.delete(id);

            if (deleted) {
                res.setStatusCode(204);
                return "";
            } else {
                res.setStatusCode(404);
                return "{\"error\": \"User not found\"}";
            }
        } catch (NumberFormatException e) {
            res.setStatusCode(400);
            return "{\"error\": \"ID must be a number\"}";
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