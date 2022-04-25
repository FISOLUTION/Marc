package fis.marc.controller;

import fis.marc.domain.Marc;
import fis.marc.dto.InputMarcRequest;
import fis.marc.dto.ParseOneResponse;
import fis.marc.dto.SaveMarcRequest;
import fis.marc.repository.MarcRepository;
import fis.marc.service.MarcService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class MarcController {

    private final MarcService marcService;
    private final MarcRepository marcRepository;

    @PostMapping("/upload")
    public void saveOrigin(@RequestBody SaveMarcRequest request) { // 원본 Marc 업로드
        marcService.saveOrigin(request);
    }

    @GetMapping("/marc/input/{userid}")
    public ParseOneResponse viewOne(@PathVariable("userid") Long userId, @RequestParam(value = "marcid", required = false) Long marcId) {
        return marcService.parseOne();
    } // 파싱한 Marc데이터 하나 던져줌

    @PostMapping("/marc/input/{userid}")
    public void inputMarc(@PathVariable("userid") Long userId,
                          @RequestBody InputMarcRequest inputMarcRequest) {
        String combinedMarc = marcService.combine(inputMarcRequest);
        marcService.saveWorked(inputMarcRequest.getMarc_id(), combinedMarc);
    } // 작업한 Marc데이터 조합

}
