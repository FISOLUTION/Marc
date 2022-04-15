package fis.marc.controller;

import fis.marc.domain.Marc;
import fis.marc.dto.ParseOneResponse;
import fis.marc.dto.SaveMarcRequest;
import fis.marc.service.MarcService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class MarcController {

    private final MarcService marcService;

    @PostMapping("/marc")
    public void saveMarc(@RequestBody SaveMarcRequest request) { // 원본 Marc 업로드
        marcService.saveMarc(request);
    }

    @GetMapping("/marc")
    public ParseOneResponse viewOne() {
        return marcService.parseOne();
    }
}
