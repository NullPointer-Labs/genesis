package application.service;

import application.exceptions.BadRequestException;
import application.exceptions.ResourceNotFoundException;
import application.model.User;
import application.repository.UserRepository;
import framework.annotations.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public List<User> findAll() {
        return repository.findAll();
    }

    public User findById(int id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    public User create(User user) {
        if (user.getName() == null || user.getName().length() < 3) {
            throw new BadRequestException("Name must have at least 3 characters");
        }

        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            throw new BadRequestException("Invalid email");
        }

        return repository.save(user);
    }

    public void delete(int id) {
        User user = findById(id);

        repository.deleteById(user.getId());
    }
}