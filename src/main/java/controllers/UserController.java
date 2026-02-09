package controllers;

import annotations.DELETE;
import annotations.GET;
import annotations.POST;
import annotations.PUT;
import http.Request;
import http.Response;

public class UserController {

    @GET("/api/users")
    public void listUsers(Request req, Response res) {
        res.setStatusCode(200);
        res.setBody("[{\"name\": \"Wesley\"}, {\"name\": \"Java\"}]");
        res.setContentType("application/json");
    }

    @POST("/api/users")
    public void createUser(Request req, Response res) {
        res.setStatusCode(201);
        res.setBody("{\"message\": \"User created\"}");
        res.setContentType("application/json");
    }

    @PUT("/api/users")
    public void updateUser(Request req, Response res) {
        String body = req.getBody();
        res.setStatusCode(200);
        res.setBody("{\"message\": \"User updated\", \"changes\": " + body + "}");
        res.setContentType("application/json");
    }

    @DELETE("/api/users")
    public void deleteUser(Request req, Response res) {
        String id = req.getParam("id");
        res.setStatusCode(200);
        res.setBody("{\"message\": \"User " + id + " deleted\"}");
        res.setContentType("application/json");
    }
}