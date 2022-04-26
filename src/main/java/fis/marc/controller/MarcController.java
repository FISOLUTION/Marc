package fis.marc.controller;

import fis.marc.domain.Marc;
import fis.marc.dto.CheckMarcRequest;
import fis.marc.dto.InputMarcRequest;
import fis.marc.dto.ParseOneResponse;
import fis.marc.dto.SaveMarcRequest;
import fis.marc.repository.MarcRepository;
import fis.marc.service.MarcService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


@RestController
@Slf4j
@RequiredArgsConstructor
public class MarcController {

    private final MarcService marcService;

    @PostMapping("/upload")
    public void saveOrigin(@RequestBody SaveMarcRequest request) { // 원본 Marc 업로드
        marcService.saveOrigin(request);
    }

    @GetMapping("/marc/input/{userid}")
    public ParseOneResponse viewOriginOne(@PathVariable("userid") Long userId,
                                          @RequestParam(value = "marcid", required = false) Long marcId) {
        if (marcId == null) {
            return marcService.parseOriginOne();
        } else {
            return marcService.findParseOriginOne(marcId);
        }
    } // 파싱한 Marc 데이터 하나 던져줌

    @PostMapping("/marc/input/{userid}")
    public void inputMarc(@PathVariable("userid") Long userId,
                          @RequestBody InputMarcRequest inputMarcRequest) {
        String combinedMarc = marcService.combine(inputMarcRequest);
        marcService.saveWorked(inputMarcRequest.getMarc_id(), userId, combinedMarc);
    } // 입력 페이지에서 작업한 Marc 데이터 조합

    @GetMapping("/marc/check/{userid}")
    public ParseOneResponse viewCheckOne(@PathVariable("userid") Long userId,
                                         @RequestParam(value = "marcid", required = false) Long marcId) {
        if (marcId == null) {
            return marcService.parseCheckedOne();
        } else {
            return marcService.findParseCheckedOne(marcId);
        }
    } // 검수 페이지에서 파싱한 Worked 데이터 하나 던져줌

    @PostMapping("/marc/check/{userid}")
    public void checkMarc(@PathVariable("userid") Long userId,
                          @RequestBody CheckMarcRequest inputMarcRequest) {
        String combinedMarc = marcService.combine(inputMarcRequest);
        marcService.saveChecked(inputMarcRequest.getMarc_id(), userId,
                combinedMarc, inputMarcRequest.getComment());
    } // 검수 페이지 저장
}
