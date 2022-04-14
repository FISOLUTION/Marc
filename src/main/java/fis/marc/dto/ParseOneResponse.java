package fis.marc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ParseOneResponse {
    private String leader;
    private String directory;
    private String data;
}
