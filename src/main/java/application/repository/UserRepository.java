package application.repository;

import application.model.User;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
    List<User> findAll();
    Optional<User> findById(int id);
    User save(User user);
    boolean deleteById(int id);
}