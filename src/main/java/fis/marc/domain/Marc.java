package fis.marc.domain;

import fis.marc.dto.SaveMarcRequest;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Entity
@Slf4j
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Marc {
    @GeneratedValue
    @Id @Column(name = "marc_id")
    private Long id;

    @Lob
    private String origin;

    @Lob
    private String worked;

    @Lob
    private String checked;

    public static Marc createMarc(String origin) {
        Marc marc = new Marc();
        marc.origin = origin;
        return marc;
    }

    public void updateWorked(String worked) {
        this.worked = worked;
    }
}

