package fis.marc.repository;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
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

    public Marc findOneOriginRandom() {
        List<Marc> marcList = em.createQuery(
                "select m from Marc m where m.worked is null and m.checked is null",
                        Marc.class)
                .getResultList();
        return marcList.get(0);
    }

    public Marc findOneCheckedRandom() {
        List<Marc> marcList = em.createQuery(
                "select m from Marc m where m.worked is not null and m.checked is null",
                        Marc.class)
                .getResultList();
        return marcList.get(0); // 조회할 게 없을 때 예외처리 필요함.
    }

    public List<Marc> findAll() {
        return em.createQuery("select m from Marc m", Marc.class)
                .getResultList();
    }
}
