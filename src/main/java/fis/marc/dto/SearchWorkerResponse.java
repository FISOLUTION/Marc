package fis.marc.dto;

import fis.marc.domain.enumType.Authority;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class SearchWorkerResponse {
    private Long userId;
    private String nickname;
    private String pwd;
    private Authority auth;
    private String username;
    private String address;
    private String phnum;
    private int workingAmount;
    private Double averageWorkingAmount;
    private Double rejectedRate;
    private List<TendencyDto> tendency = new ArrayList<>();

    @Data
    @AllArgsConstructor
    public static class TendencyDto {
        private String date;
        private int workingAmount;
    }

    public SearchWorkerResponse(Long userId, String nickname, String pwd, Authority auth, String username, String address, String phnum, int workingAmount, Double averageWorkingAmount, Double rejectedRate) {
        this.userId = userId;
        this.nickname = nickname;
        this.pwd = pwd;
        this.auth = auth;
        this.username = username;
        this.address = address;
        this.phnum = phnum;
        this.workingAmount = workingAmount;
        this.averageWorkingAmount = averageWorkingAmount;
        this.rejectedRate = rejectedRate;
    }
}
