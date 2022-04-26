package fis.marc.repository;

import fis.marc.domain.Marc;
import fis.marc.domain.Process;
import fis.marc.domain.enumType.Authority;
import fis.marc.domain.enumType.Status;
import fis.marc.dto.WorkListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProcessRepository {
    private final EntityManager em;

    public Optional<Process> findOne(Long id) {
        return Optional.ofNullable(em.find(Process.class, id));
    }

    public void saveProcess(Process process) {
        em.persist(process);
    }

    public List<Process> findAllByUserId(Long userId) {
        return em.createQuery("select p from Process p join fetch p.user u where u.id = :userId", Process.class)
                .setParameter("userId", userId)
                .getResultList();

    }

    public List<Process> findAll() {
        return em.createQuery("select p from Process p", Process.class)
                .getResultList();
    }

    public List<Process> findProgressingWorkByUserId(Long userId, Authority authority) {
        Status status = null;
        if (authority == Authority.Worker) {
            status = Status.Input;
        } else if (authority == Authority.Checker) {
            status = Status.Check;
        }
        return em.createQuery("select p from Process p join fetch p.user u" +
                " where u.id =: userId and p.status = :status", Process.class)
                .setParameter("userId", userId)
                .setParameter("status", status)
                .getResultList();
    }

    public int totalSize() {
        return findAll().size();
    }

    public int checkSize() {
        List<Process> processes = em.createQuery("select p from Process p where p.status = :status", Process.class)
                .setParameter("status", Status.Check)
                .getResultList();
        return processes.size();
    }

    public int InputSize() {
        List<Process> processes = em.createQuery("select p from Process p where p.status = :status", Process.class)
                .setParameter("status", Status.Input)
                .getResultList();
        return processes.size();
    }
}
