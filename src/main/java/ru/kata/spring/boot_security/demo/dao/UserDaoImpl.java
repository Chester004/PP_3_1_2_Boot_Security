package ru.kata.spring.boot_security.demo.dao;

import javax.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public class UserDaoImpl implements UserDao {

    private final EntityManager em;

    @Autowired
    public UserDaoImpl(EntityManager em) {
        this.em = em;
    }

    public List<User> getAllUsers() {
        return em.createQuery("FROM User", User.class).getResultList();
    }

    public void save(User user) {
        em.persist(user);
    }

    public User show(Long id) {
        return em.find(User.class, id);
    }

    public Optional<User> findByEmail(String email) {
       Optional<User> user = em.createQuery("FROM User where email = :email", User.class).setParameter("email", email).getResultStream().findAny();
       if(user.isPresent()){
           System.out.println(user.get().getRoles());
       }
        return user;
    }
    public void update(User updatedUser) {
        em.merge(updatedUser);
    }

    public void delete(Long id) {
        em.remove(show(id));
    }
}
