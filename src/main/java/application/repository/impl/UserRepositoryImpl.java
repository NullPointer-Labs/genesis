package application.repository.impl;

import application.db.Database;
import application.model.User;
import application.repository.UserRepository;
import framework.annotations.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {

    @Override
    public List<User> findAll() {
        return Database.findAll();
    }

    @Override
    public Optional<User> findById(int id) {
        return Optional.ofNullable(Database.findById(id));
    }

    @Override
    public User save(User user) {
        return Database.save(user);
    }

    @Override
    public boolean deleteById(int id) {
        return Database.delete(id);
    }
}