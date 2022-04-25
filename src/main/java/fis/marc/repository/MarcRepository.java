package fis.marc.repository;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import fis.marc.domain.Marc;
import fis.marc.domain.QMarc;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static fis.marc.domain.QMarc.marc;

@Repository
@RequiredArgsConstructor
public class MarcRepository {

    private final EntityManager em;
    private final JPAQueryFactory jpaQueryFactory;

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

    public List<Marc> findAll() {
        return em.createQuery("select m from Marc m", Marc.class)
                .getResultList();
    }
}
