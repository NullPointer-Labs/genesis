package application.controller;

import application.exceptions.BadRequestException;
import application.model.User;
import application.service.UserService;
import com.google.gson.Gson;
import framework.annotations.*;
import framework.http.Request;
import framework.http.Response;

import java.util.List;

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
    public User getUser(Request req, Response res) {
        String idParam = req.getParam("id");

        if (idParam == null) {
            throw new BadRequestException("Query param 'id' is required");
        }

        try {
            int id = Integer.parseInt(idParam);
            return userService.findById(id);
        } catch (NumberFormatException e) {
            throw new BadRequestException("ID must be a number");
        }
    }

    @POST("/api/users")
    public User createUser(Request req, Response res) {
        String body = req.getBody();
        if (body == null || body.isEmpty()) {
            throw new BadRequestException("Body is required");
        }
        User newUser = gson.fromJson(body, User.class);
        User savedUser = userService.create(newUser);
        res.setStatusCode(201);
        return savedUser;
    }

    @DELETE("/api/users")
    public void deleteUser(Request req, Response res) {
        String idParam = req.getParam("id");
        if (idParam == null) throw new BadRequestException("ID required");

        try {
            int id = Integer.parseInt(idParam);
            userService.delete(id);
            res.setStatusCode(204);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Invalid ID");
        }
    }

}