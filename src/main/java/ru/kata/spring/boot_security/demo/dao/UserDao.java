package ru.kata.spring.boot_security.demo.dao;



import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    List<User> getAllUsers();

    void save(User user);

    User show(Long id);
    Optional<User> findByEmail(String email);
    void update(User updatedUser);

    void delete(Long id);
}
