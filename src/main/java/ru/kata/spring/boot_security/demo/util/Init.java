package ru.kata.spring.boot_security.demo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

@Component
public class Init {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public Init(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @PostConstruct
    public void initializedDataBase() {
        Role roleAdmin = new Role("ROLE_ADMIN");
        Role roleUser =  new Role("ROLE_USER");
        Set<Role> adminRole = new HashSet<>();
        Set<Role> userRole = new HashSet<>();
        adminRole.add(roleAdmin);
        adminRole.add(roleUser);

        userRole.add(roleUser);
        User admin = new User("admin","admin","admin@mail.ru", "admin");
        User user = new User("user","user","user@mail.ru","user");
        admin.setRoles(adminRole);
        user.setRoles(userRole);
        userService.save(admin);
        userService.save(user);
    }
}
