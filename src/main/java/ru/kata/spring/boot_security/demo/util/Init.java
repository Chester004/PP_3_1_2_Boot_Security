package ru.kata.spring.boot_security.demo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

@Component
public class Init {

    private final UserService userService;

    @Autowired
    public Init(UserService userService) {
        this.userService = userService;
    }

    @PostConstruct
    public void initializedDataBase() {
        Role roleAdmin = new Role("ROLE_ADMIN");
        Role roleUser =  new Role("ROLE_USER");
        Set<Role> adminRole = new HashSet<>();
        Set<Role> userRole = new HashSet<>();
        System.out.println(adminRole.add(roleAdmin));
        System.out.println(adminRole.add(roleUser));

        userRole.add(roleUser);

        User admin = new User("admin","admin","admin@mail.ru", "admin");
        User user = new User("user","user","user@mail.ru","user");

        admin.setRoles(adminRole);
        user.setRoles(userRole);
        System.out.println(admin);
        System.out.println(user);
        userService.save(admin);
        userService.save(user);
    }
}
