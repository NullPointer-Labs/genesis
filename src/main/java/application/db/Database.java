package application.db;

import application.model.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Database {
    private static final List<User> users = Collections.synchronizedList(new ArrayList<>());

    private static final AtomicInteger idGenerator = new AtomicInteger(1);

    static {
        users.add(new User(idGenerator.getAndIncrement(), "Wesley", "wesley@java.com"));
        users.add(new User(idGenerator.getAndIncrement(), "Maria", "maria@java.com"));
        users.add(new User(idGenerator.getAndIncrement(), "João", "joao@java.com"));
    }


    public static List<User> findAll() {
        return new ArrayList<>(users);
    }

    public static User findById(int id) {
        return users.stream()
                .filter(u -> u.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public static User save(User user) {
        if (user.getId() == 0) {
            user.setId(idGenerator.getAndIncrement());
            users.add(user);
        } else {
            delete(user.getId());
            users.add(user);
        }
        return user;
    }

    public static boolean delete(int id) {
        return users.removeIf(u -> u.getId() == id);
    }
}