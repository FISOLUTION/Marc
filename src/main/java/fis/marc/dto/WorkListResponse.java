package fis.marc.dto;

import fis.marc.domain.enumType.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkListResponse {
    private List<WorkDto> workDtoList = new ArrayList<>();

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class WorkDto {
        private Long marc_id;
        private String leader;
        private String createDate;
        private Status status;
        private String comment;
    }


}
