package fis.marc.repository;

import fis.marc.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepository {
    private final EntityManager em;

    public Optional<User> findOne(Long id) {
        return Optional.ofNullable(em.find(User.class, id));
    }

}
