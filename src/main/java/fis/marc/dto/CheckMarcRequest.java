package fis.marc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Lob;
import java.util.ArrayList;
import java.util.List;

/**
 * @implNote 기존 InputMarcRequest 에서 Comment 정보만 추가
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckMarcRequest extends InputMarcRequest {
    private String comment;
}
