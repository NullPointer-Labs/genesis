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
}