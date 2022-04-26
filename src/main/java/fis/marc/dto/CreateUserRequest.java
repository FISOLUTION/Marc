package fis.marc.dto;

import fis.marc.domain.enumType.Authority;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRequest {
    private String nickname;
    private String pwd;
    private Authority auth;
    private String username;
    private String address;
    private String phnum;

}
