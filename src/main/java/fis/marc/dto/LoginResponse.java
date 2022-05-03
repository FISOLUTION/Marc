package fis.marc.dto;

import fis.marc.domain.enumType.Authority;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginResponse {
    private Authority auth;
    private String username;
    private Long user_id;
}
