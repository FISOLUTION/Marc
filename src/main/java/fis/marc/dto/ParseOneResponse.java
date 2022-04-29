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

    private List<Integer> field_length_List;
    private List<Integer> field_start_List;
    private List<String> indicator_List;

    private String comment;
}
