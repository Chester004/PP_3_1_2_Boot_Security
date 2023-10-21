package ru.kata.spring.boot_security.demo.util;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.Optional;


@Component
public class UserValidator implements Validator {

    private final UserService userService;

    @Autowired
    public UserValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
        Optional<User> bdUser = userService.findByEmail(user.getEmail());
        System.err.println(user);
        System.err.println(bdUser.isPresent());
        if ((bdUser.isPresent() && user.getId() == null)
                || (bdUser.isPresent() && !user.getId().equals(bdUser.get().getId()))) {
            System.err.println("Прошел");
            errors.rejectValue("email", "", "Email is already taken");
        }
    }
}
