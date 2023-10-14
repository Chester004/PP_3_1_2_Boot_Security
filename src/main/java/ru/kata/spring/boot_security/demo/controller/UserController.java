package ru.kata.spring.boot_security.demo.controller;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.dao.RoleDao;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.util.UserValidator;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;


@Controller
@RequestMapping
public class UserController {

    private final UserService userService;
    private final UserValidator validator;
    private final RoleDao roleDao;

    @Autowired
    public UserController(UserService userService, UserValidator validator, RoleDao roleDao) {
        this.userService = userService;
        this.validator = validator;
        this.roleDao = roleDao;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "users/all";
    }

    @GetMapping("/new")
    public String newUser(Model model, @ModelAttribute("user") User user) {
        model.addAttribute("allRoles", roleDao.getAllRole());
        return "users/new";
    }

    @PostMapping
    public String create(@ModelAttribute("user") @Valid User user, BindingResult bindingResult,
                        @RequestParam(value = "roles", defaultValue = "1") Integer[] roles) {
        validator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            return "users/new";
        }
        if (roles != null) {
            Set<Role> selectedRoles = Arrays.stream(roles)
                    .map(roleDao::findById)
                    .collect(Collectors.toSet());
            user.setRoles(selectedRoles);
        }
        userService.save(user);
        return "redirect:/";
    }

    @GetMapping("/edit")
    public String edit(Model model, @RequestParam("id") Long id) {
        model.addAttribute("user", userService.show(id));
        return "users/edit";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("user") @Valid User user, BindingResult bindingResult) {
        validator.validate(user, bindingResult);

        if (bindingResult.hasErrors()) {
            return "users/edit";
        }

        userService.update(user);
        return "redirect:/";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam("id") Long id) {
        if (userService.show(id) != null) {
            userService.delete(id);
        }
        return "redirect:/";
    }

}
