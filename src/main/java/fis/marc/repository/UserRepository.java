package fis.marc.repository;

import fis.marc.domain.User;
import fis.marc.domain.enumType.Authority;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
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

    public Optional<User> findOneByNickname(String nickname) {
        List<User> result = em.createQuery("select u from User u where u.nickname =: nickname", User.class)
                .setParameter("nickname", nickname)
                .getResultList();
        return result.stream().findAny();
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

    public List<User> findAllUserByDate(String date, Authority auth) {
        return em.createQuery("select u from User u " +
                        "where u.createDate = :date and u.auth = :auth", User.class)
                .setParameter("date", date)
                .setParameter("auth", auth)
                .getResultList();
    }

    public List<User> findAllUserByMonth(Integer monthValue, Authority auth) {
        String month;
        if (monthValue < 10) {
            month = "______" + monthValue.toString() + "___";
        } else {
            month = "_____" + monthValue.toString() + "___";
        }
        return em.createQuery("select u from User u " +
                        "where u.createDate like :month and u.auth = :auth", User.class)
                .setParameter("month", month)
                .setParameter("auth", auth)
                .getResultList();
    }

    public List<User> findAllUserByWeek(String startDate, String endDate, Authority auth) {
        return em.createQuery("select u from User u " +
                        "where u.createDate between :startDate and :endDate and u.auth = :auth", User.class)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .setParameter("auth", auth)
                .getResultList();
    }
}
