package fis.marc.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Entity
@Slf4j
@Getter
@NoArgsConstructor
public class Marc {
    @GeneratedValue @Id
    private Long id;

    @Lob
    private String content;     // marc 내용

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="parsed_marc")
    private ParsedMarc parsedMarc;
}

