package fis.marc.repository;

import fis.marc.domain.Marc;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MarcRepository {

    private final EntityManager em;

    public Optional<Marc> findOne(Long id) {
        return Optional.ofNullable(em.find(Marc.class, id));
    }

    public void save(Marc content){
        em.persist(content);
    }
}
