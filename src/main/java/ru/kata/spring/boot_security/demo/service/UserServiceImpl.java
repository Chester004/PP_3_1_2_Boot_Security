package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Lazy
    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public void save(User user){
        user.setRoles(user.getRoles().stream().map(role -> roleService.findById(role.getId())).collect(Collectors.toList()));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public User show(Long id) {
        return userRepository.findById(id).get();
    }

    @Override
    @Transactional
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional
    public void update(User user) {
        user.setRoles(user.getRoles().stream().map(role -> roleService.findById(role.getId())).collect(Collectors.toList()));
        user.setPassword(passwordEncoder.encode(user.getPassword()));// Двойной кодинг
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (show(id) != null) {
            userRepository.deleteById(id);
        }
    }
}
