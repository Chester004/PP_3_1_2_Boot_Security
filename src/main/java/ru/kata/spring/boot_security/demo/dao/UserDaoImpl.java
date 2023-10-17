package ru.kata.spring.boot_security.demo.dao;

import javax.persistence.EntityManager;

import jdk.jfr.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public class UserDaoImpl implements UserDao {

    private final EntityManager em;

    private PasswordEncoder passwordEncoder;



    @Autowired
    public UserDaoImpl(EntityManager em) {
        this.em = em;
    }
    @Lazy
    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAllUsers() {
        return em.createQuery("FROM User", User.class).getResultList();
    }

    public void save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        em.persist(user);
    }

    public User show(Long id) {
        return em.find(User.class, id);
    }

    public Optional<User> findByEmail(String email) {
       Optional<User> user = em.createQuery("select distinct u from User AS u left join fetch u.roles where u.email = :email", User.class)
               .setParameter("email", email).getResultStream().findAny();
        return user;
    }
    public void update(User updatedUser) {
        updatedUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        em.merge(updatedUser);
    }

    public void delete(Long id) {
        em.remove(show(id));
    }
}
