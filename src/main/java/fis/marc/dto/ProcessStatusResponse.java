package fis.marc.dto;

import fis.marc.domain.Process;
import fis.marc.domain.enumType.Authority;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProcessStatusResponse {
    private int total;
    private int check;
    private int input;
    private List<ProcessDto> performance = new ArrayList<>();

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProcessDto {
        private String username;
        private String workingDate;
        private int workingAmount;
        private Authority auth;
    }

    public ProcessStatusResponse(int total, int check, int input) {
        this.total = total;
        this.check = check;
        this.input = input;
    }
}
