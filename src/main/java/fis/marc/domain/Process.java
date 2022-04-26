package fis.marc.domain;

import fis.marc.domain.enumType.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Getter
@Entity
@Slf4j
public class Process {
    @Id
    @GeneratedValue
    @Column(name = "process_id")
    private Long id;

    private String createdDate; // 생성시점

    @Enumerated(EnumType.STRING)
    private Status status;      // 현재 공정 상태

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="marc_id")
    private Marc marc;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="worker_id")
    private User user; // 입력자, 검수자

    public void updateUser(User user) {
        this.user = user;
    }

    protected Process() {

    }

    public Process(String createdDate, Status status, Marc marc, User user) {
        this.createdDate = createdDate;
        this.status = status;
        this.marc = marc;
        this.user = user;
        user.getProcesses().add(this);
    }
}
