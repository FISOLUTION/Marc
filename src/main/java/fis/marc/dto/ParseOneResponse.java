package fis.marc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ParseOneResponse {
    private String leader;
    private List<String> directory;
    private List<String> data;
}
