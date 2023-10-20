package ru.kata.spring.boot_security.demo.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.util.UserValidator;


@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final UserValidator validator;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, UserValidator validator, RoleService roleService) {
        this.userService = userService;
        this.validator = validator;
        this.roleService = roleService;
    }

    @GetMapping
    public String index(Model model, @ModelAttribute("user") User user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        model.addAttribute("person", userService.findByEmail(currentPrincipalName).get());
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("allRoles", roleService.findAll());
        return "admin/all";
    }

    @GetMapping("/new")
    public String newUser(Model model, @ModelAttribute("user") User user) {
        model.addAttribute("allRoles", roleService.findAll());
        return "admin/new";
    }

    @PostMapping
    public String create(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, Model model) {
        System.err.println("User is :" + user.getEmail());
        validator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentPrincipalName = authentication.getName();
            model.addAttribute("person", userService.findByEmail(currentPrincipalName).get());
            model.addAttribute("users", userService.getAllUsers());
            model.addAttribute("allRoles", roleService.findAll());
            return "admin/all";
        }
        userService.save(user);
        return "redirect:/admin/";
    }

    @GetMapping("/edit")
    public String edit(Model model, @RequestParam("id") Long id) {
        model.addAttribute("user", userService.show(id));
        model.addAttribute("allRoles", roleService.findAll());
        return "admin/edit";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, Model model) {
        System.err.println(user);
        validator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentPrincipalName = authentication.getName();
            model.addAttribute("person", userService.findByEmail(currentPrincipalName).get());
            model.addAttribute("users", userService.getAllUsers());
            model.addAttribute("allRoles", roleService.findAll());
            return "admin/all";
        }
        userService.update(user);
        return "redirect:/admin/";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam("id") Long id) {
        userService.delete(id);
        return "redirect:/admin/";
    }

}
