package fis.marc.domain;

import fis.marc.domain.enumType.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Getter
@NoArgsConstructor
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
    private User user; // 입력자

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name="checker_id")
//    private User user2; // 검수자
}
