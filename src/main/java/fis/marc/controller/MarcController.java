package fis.marc.controller;

import fis.marc.domain.Marc;
import fis.marc.domain.User;
import fis.marc.domain.enumType.Authority;
import fis.marc.dto.CheckMarcRequest;
import fis.marc.dto.InputMarcRequest;
import fis.marc.dto.ParseOneResponse;
import fis.marc.dto.SaveMarcRequest;
import fis.marc.repository.MarcRepository;
import fis.marc.service.MarcService;
import fis.marc.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


@RestController
@Slf4j
@RequiredArgsConstructor
public class MarcController {

    private final MarcService marcService;
    private final UserService userService;

    @PostMapping("/upload")
    public void saveOrigin(@RequestBody SaveMarcRequest request) { // 원본 Marc 업로드
        marcService.saveOrigin(request);
    }

    @GetMapping("/marc/input/{userid}")
    public ParseOneResponse viewOriginOne(@PathVariable("userid") Long userId,
                                          @RequestParam(value = "marcid", required = false) Long marcId) {
        if (marcId == null) { // marcId 가 없으면 랜덤으로 조회
            return marcService.parseOriginOne();
        } else { // marcId 에 해당하는 marc 조회
            return marcService.findParseOriginOne(marcId);
        }
    } // 작업 안 된 데이터(Worked X) 중에서 파싱한 Marc 데이터를 하나 보냄.

    @PostMapping("/marc/input/{userid}")
    public void inputMarc(@PathVariable("userid") Long userId,
                          @RequestBody InputMarcRequest inputMarcRequest) {
        String combinedMarc = marcService.combine(inputMarcRequest);
        marcService.saveWorked(inputMarcRequest.getMarc_id(), userId, combinedMarc);
    } // 입력 페이지에서 작업한 Marc 데이터 조합
    // 같은 marc 에 대해 저장이 들어오면 process 가 하나 또 생겨서 중복되는데
    // -> 어차피 입력 완료된 데이터는 입력자가 조회할 수 없는데 따로 또 예외처리를 해줘야하나?

    @GetMapping("/marc/check/{userid}")
    public ParseOneResponse viewCheckOne(@PathVariable("userid") Long userId,
                                         @RequestParam(value = "marcid", required = false) Long marcId) throws IllegalAccessException {
        User user = userService.findUser(userId);
        if (user.getAuth() == Authority.Worker) {
            throw new IllegalAccessException("권한 없는 유저(작업자가 검수페이지에 접근)");
        }
        if (marcId == null) {
            return marcService.parseCheckedOne();
        } else {
            return marcService.findParseCheckedOne(marcId);
        }
    } // 검수 페이지에서 파싱한 Worked 데이터 하나 던져줌

    @PostMapping("/marc/modify/{userid}")
    public void ModifyRequestMarc(@PathVariable("userid") Long userId,
                          @RequestBody CheckMarcRequest inputMarcRequest) throws IllegalAccessException {
        User user = userService.findUser(userId);
        if (user.getAuth() == Authority.Worker) {
            throw new IllegalAccessException("권한 없는 유저(작업자가 검수페이지에 접근)");
        }
        marcService.updateModifyState(inputMarcRequest.getMarc_id(),
                userId, inputMarcRequest.getComment());
    } // 검수 페이지 수정 요청

    @PostMapping("/marc/check/{userid}")
    public void checkMarc(@PathVariable("userid") Long userId,
                          @RequestBody CheckMarcRequest inputMarcRequest) throws IllegalAccessException {
        User user = userService.findUser(userId);
        if (user.getAuth() == Authority.Worker) {
            throw new IllegalAccessException("권한 없는 유저(작업자가 검수페이지에 접근)");
        }
        String combinedMarc = marcService.combine(inputMarcRequest);
        marcService.saveChecked(inputMarcRequest.getMarc_id(), userId,
                combinedMarc, inputMarcRequest.getComment());
    } // 검수 페이지 저장
}


