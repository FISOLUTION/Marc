package fis.marc.repository;

import fis.marc.domain.Process;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProcessRepository {
    private final EntityManager em;

    public Optional<Process> findOne(Long id) {
        return Optional.ofNullable(em.find(Process.class, id));
    }
}
