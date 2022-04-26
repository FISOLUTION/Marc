package fis.marc.domain;


import fis.marc.domain.enumType.Authority;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Slf4j
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @GeneratedValue @Id
    @Column(name = "user_id")
    private Long id;

    private String nickname;

    private String pwd;

    @Enumerated(EnumType.STRING)
    private Authority auth;     // 권한

    private String username;

    private String address;

    private String phnum;

    private String createDate;

    @OneToMany(mappedBy = "user")
    private List<Process> processes = new ArrayList<>();

    public static User createUser(String nickname, String pwd, Authority auth,
                                  String username, String address, String phnum, String createDate) {
        User user = new User();
        user.nickname = nickname;
        user.pwd = pwd;
        user.address = address;
        user.phnum = phnum;
        user.username = username;
        user.auth = auth;
        user.createDate = createDate;
        return user;
    }

    public void updateInfo(String nickname, String pwd, Authority auth, String username,
                           String address, String phnum) {
        this.nickname = nickname;
        this.pwd = pwd;
        this.auth = auth;
        this.username = username;
        this.address = address;
        this.phnum = phnum;
    }
}
