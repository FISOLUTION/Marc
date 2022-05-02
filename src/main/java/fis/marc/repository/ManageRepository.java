package fis.marc.repository;

import fis.marc.domain.Manage;
import fis.marc.domain.Process;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ManageRepository {
    private final EntityManager em;

    public void saveManage(Manage manage) {
        em.persist(manage);
    }

    public Optional<Manage> findAll() {
        List<Manage> resultList = em.createQuery("select m from Manage m", Manage.class)
                .getResultList();
        return resultList.stream().findAny();
    }
}
