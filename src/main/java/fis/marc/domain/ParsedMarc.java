package fis.marc.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@Slf4j
@Entity
@NoArgsConstructor
public class ParsedMarc {

    @Id @GeneratedValue
    private Long id;

    private String leader;      // leader 정보

    private String directory;   // directory 정보

    private String data;        // data 정보
}
