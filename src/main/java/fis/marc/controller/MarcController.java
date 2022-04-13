package fis.marc.controller;

import fis.marc.dto.SaveMarcRequest;
import fis.marc.service.MarcService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class MarcController {

    private final MarcService marcService;

    @PostMapping("/marc")
    public void saveMarc(@RequestBody SaveMarcRequest request) {
        log.warn("{}", request.getData());
        marcService.saveMarc(request);
    }
}
