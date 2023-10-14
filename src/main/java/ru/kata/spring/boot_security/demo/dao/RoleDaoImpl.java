package ru.kata.spring.boot_security.demo.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class RoleDaoImpl implements RoleDao {

    private final EntityManager em;

    @Autowired
    public RoleDaoImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    @Transactional
    public List<Role> getAllRole() {
        return em.createQuery("FROM Role ", Role.class).getResultList();
    }

    @Override
    @Transactional
    public Role findById(Integer roleId) {
        return em.find(Role.class, roleId);
    }
}
