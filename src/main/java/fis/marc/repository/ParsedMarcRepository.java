package fis.marc.repository;

import fis.marc.domain.ParsedMarc;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ParsedMarcRepository {

    private final EntityManager em;

    public Optional<ParsedMarc> findOne(Long id) {
        return Optional.ofNullable(em.find(ParsedMarc.class, id));
    }
}
