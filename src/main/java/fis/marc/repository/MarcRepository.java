package fis.marc.repository;

import fis.marc.domain.Marc;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MarcRepository {

    private final EntityManager em;

    public Optional<Marc> findOne(Long id) {
        return Optional.ofNullable(em.find(Marc.class, id));
    }

    public void saveOrigin(Marc content){
        em.persist(content);
    }

    public void saveWorked(Marc content){
        em.persist(content);
    }

    public Marc findOneOfAll() {
        List<Marc> marcList = em.createQuery("select m from Marc m", Marc.class)
                .getResultList();
        return marcList.get(0);
    }
}
