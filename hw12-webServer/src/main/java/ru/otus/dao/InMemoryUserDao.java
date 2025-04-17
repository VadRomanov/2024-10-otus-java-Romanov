package ru.otus.dao;

import ru.otus.crm.model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryUserDao implements UserDao {

    private static final String DEFAULT_PASSWORD = "11111";
    private final Map<Long, User> users;

    public InMemoryUserDao() {
        users = new HashMap<>();
        users.put(1L, new User(1L, "Крис Гир", "admin", DEFAULT_PASSWORD));
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return users.values().stream().filter(v -> v.getLogin().equals(login)).findFirst();
    }
}
