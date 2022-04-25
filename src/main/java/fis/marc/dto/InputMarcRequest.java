package fis.marc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Lob;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class InputMarcRequest {
    private Long marc_id;
    private List<InputMarcDto> dataList = new ArrayList<>();

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class InputMarcDto {
        private String indicator;
        @Lob
        private String data;
    }
}
