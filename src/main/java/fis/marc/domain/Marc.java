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
    private String origin; // 원본

    @Lob
    private String worked; // 입력 후

    @Lob
    private String checked; // 검수 후

    private String comment;

    public static Marc createMarc(String origin) {
        Marc marc = new Marc();
        marc.origin = origin;
        return marc;
    }

    public void updateWorked(String worked) {
        this.worked = worked;
    }

    public void updateChecked(String checked) {
        this.checked = checked;
    }

    public void updateComment(String comment) {
        this.comment = comment;
    }
}

