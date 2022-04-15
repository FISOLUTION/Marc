package fis.marc.domain;


import fis.marc.domain.enumType.Authority;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Slf4j
@Getter
@NoArgsConstructor
public class User {
    @GeneratedValue @Id
    @Column(name = "user_id")
    private Long id;

    private String username;

    @Enumerated
    private Authority auth;     // 권한

    @OneToMany(mappedBy = "user")
    private List<Process> processes = new ArrayList<>();


}
