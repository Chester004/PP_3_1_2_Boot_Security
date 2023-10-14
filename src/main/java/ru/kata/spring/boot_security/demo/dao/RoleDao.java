package ru.kata.spring.boot_security.demo.dao;

import ru.kata.spring.boot_security.demo.model.Role;

import javax.swing.*;
import java.util.List;

public interface RoleDao {
    public List<Role> getAllRole();

    Role findById(Integer roleId);
}
