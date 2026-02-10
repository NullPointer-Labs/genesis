package application.controller;

import application.exceptions.BadRequestException;
import application.exceptions.ResourceNotFoundException;
import application.model.User;
import application.service.UserService;
import com.google.gson.Gson;
import framework.annotations.*;
import framework.http.Request;
import framework.http.Response;

import java.util.List;
import java.util.Map;

@RestController
public class UserController {

    private final Gson gson = new Gson();
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GET("/api/users")
    public List<User> listUsers() {
        return userService.findAll();
    }

    @GET("/api/users/detail")
    public Object getUser(Request req, Response res) {
        String idParam = req.getParam("id");

        if (idParam == null) {
            res.setStatusCode(400);
            return Map.of("error", "ID is required");
        }

        try {
            int id = Integer.parseInt(idParam);
            return userService.findById(id);
        } catch (NumberFormatException e) {
            res.setStatusCode(400);
            return Map.of("error", "ID must be a number");
        } catch (ResourceNotFoundException e) {
            res.setStatusCode(404);
            return Map.of("error", e.getMessage());
        }
    }

    @POST("/api/users")
    public Object createUser(Request req, Response res) {
        String body = req.getBody();
        if (body == null || body.isEmpty()) {
            res.setStatusCode(400);
            return Map.of("error", "Body is required");
        }

        try {
            User newUser = gson.fromJson(body, User.class);
            User savedUser = userService.create(newUser);
            res.setStatusCode(201);
            return savedUser;
        } catch (BadRequestException e) {
            res.setStatusCode(400);
            return Map.of("error", e.getMessage());
        } catch (Exception e) {
            res.setStatusCode(500);
            return Map.of("error", "Internal Error");
        }
    }

    @DELETE("/api/users")
    public Object deleteUser(Request req, Response res) {
        String idParam = req.getParam("id");
        if (idParam == null) {
            res.setStatusCode(400);
            return Map.of("error", "ID is required");
        }

        try {
            int id = Integer.parseInt(idParam);
            userService.delete(id);
            res.setStatusCode(204);
            return null;
        } catch (NumberFormatException e) {
            res.setStatusCode(400);
            return Map.of("error", "ID must be a number");
        } catch (ResourceNotFoundException e) {
            res.setStatusCode(404);
            return Map.of("error", e.getMessage());
        }
    }
}