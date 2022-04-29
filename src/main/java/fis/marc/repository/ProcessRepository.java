package fis.marc.repository;

import fis.marc.domain.Marc;
import fis.marc.domain.Process;
import fis.marc.domain.User;
import fis.marc.domain.enumType.Authority;
import fis.marc.domain.enumType.Status;
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
        return em.createQuery("select p from Process p join fetch p.user u join fetch p.checker c " +
                        "where u.id = :userId or c.id = :userId", Process.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    public List<Process> findWorkingAmountByUserId(Long userId) {
        return em.createQuery("select p from Process p join fetch p.user u " +
                        "where u.id = :userId and p.status <> :status", Process.class)
                .setParameter("userId", userId)
                .setParameter("status", Status.Upload)
                .getResultList();
    }

    public Process findOneByUser(User user) {
        return em.createQuery("select p from Process p where p.user = :user", Process.class)
                .setParameter("user", user)
                .getSingleResult();
    }

    public List<Process> findModifyOneByMarc(Marc marc) {
        List<Process> processes = em.createQuery("select p from Process p where p.marc = :marc and p.status = :status", Process.class)
                .setParameter("marc", marc)
                .setParameter("status", Status.Modify)
                .getResultList();
        return processes;
    }

    public Optional<Process> findWorkedOneByMarc(Marc marc) {
        List<Process> processes = em.createQuery("select p from Process p " +
                        "where p.marc = :marc and p.status = :status", Process.class)
                .setParameter("marc", marc)
                .setParameter("status", Status.Input)
                .getResultList();
        return processes.stream().findAny();
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

    public List<Process> findUploadedAll() {
        return em.createQuery("select p from Process p where p.status = :status", Process.class)
                .setParameter("status", Status.Upload)
                .getResultList();
    }

    public int totalSize() {
        return findUploadedAll().size();
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

    public List<Process> findAllProcessByDate(String date, Status status) {
        return em.createQuery("select p from Process p " +
                        "where p.createdDate = :date and p.status = :status", Process.class)
                .setParameter("date", date)
                .setParameter("status", status)
                .getResultList();
    }

    public List<Process> findAllProcessByMonth(Integer monthValue, Status status) {
        String month;
        if (monthValue < 10) {
            month = "______" + monthValue.toString() + "___";
        } else {
            month = "_____" + monthValue.toString() + "___";
        }
        return em.createQuery("select p from Process p " +
                        "where p.createdDate like :month and p.status = :status", Process.class)
                .setParameter("month", month)
                .setParameter("status", status)
                .getResultList();
    }

    public List<Process> findAllProcessByWeek(String startDate, String endDate, Status status) {
        return em.createQuery("select p from Process p " +
                        "where p.createdDate between :startDate and :endDate and p.status = :status", Process.class)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .setParameter("status", status)
                .getResultList();
    }

    public List<Process> findCheckedAll() {
        return em.createQuery("select p from Process p where p.status = :status", Process.class)
                .setParameter("status", Status.Check)
                .getResultList();
    }

    public List<Process> findModifyAllByWorker(User user) {
        return em.createQuery("select p from Process p " +
                "where p.user = :user and p.checker is not null", Process.class)
                .setParameter("user", user)
                .getResultList();
    }
}
