package fis.marc.repository;

import fis.marc.domain.User;
import fis.marc.domain.enumType.Authority;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepository {
    private final EntityManager em;

    public Long save(User user) {
        em.persist(user);
        return user.getId();
    }

    public Optional<User> findOne(Long id) {
        return Optional.ofNullable(em.find(User.class, id));
    }

    public void delete(User user) {
        em.remove(user);
    }

    public List<User> findAll() {
        return em.createQuery("select u from User u", User.class)
                .getResultList();
    }

    public List<User> findAllWorker() {
        return em.createQuery("select u from User u where u.auth = :auth", User.class)
                .setParameter("auth", Authority.Worker)
                .getResultList();
    }

    public List<User> findAllChecker() {
        return em.createQuery("select u from User u where u.auth = :auth", User.class)
                .setParameter("auth", Authority.Checker)
                .getResultList();
    }
}
