package fis.marc.controller;

import fis.marc.domain.Marc;
import fis.marc.domain.User;
import fis.marc.domain.enumType.Authority;
import fis.marc.dto.CheckMarcRequest;
import fis.marc.dto.InputMarcRequest;
import fis.marc.dto.ParseOneResponse;
import fis.marc.dto.SaveMarcRequest;
import fis.marc.exception.NoAuthorityException;
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

    /**
     * @implNote 마크 데이터 업로드
     * @param request 업로드한 유저와 marc 데이터 정보
     * @author 이창윤
     */
    @PostMapping("/upload")
    public void saveOrigin(@RequestBody SaveMarcRequest request) { // 원본 Marc 업로드
        marcService.saveOrigin(request);
    }

    /**
     * @implNote 데이터 불러옴 - Upload(새로 입력할 데이터), Input(입력이 완료된 데이터), Modify(수정해야 하는 데이터)
     * Upload 는 랜덤으로 가져와 작업할 데이터, Input 과 Modify 는 작업 목록 확인을 위해 필요
     * @param userId 요청한 유저 정보
     * @param marcId 존재하면 해당 마크 조회, null 이면 랜덤 마크 조회
     * @author 이창윤
     * @return 파싱된 마크의 origin
     */
    @GetMapping("/marc/input/{userid}")
    public ParseOneResponse viewOriginOne(@PathVariable("userid") Long userId,
                                          @RequestParam(value = "marcid", required = false) Long marcId) {
        if (marcId == null) { // marcId 가 없으면 랜덤으로 조회
            return marcService.parseOriginOne();
        } else { // marcId 에 해당하는 marc 조회
            return marcService.findParseOriginOne(marcId);
        }
    } // 작업 안 된 데이터(Worked X) 중에서 파싱한 Marc 데이터를 하나 보냄.

    /**
     * @implNote 입력받은 마크 데이터를 재조합 후 worked 에 저장 & Input 프로세스 insert
     * @param userId 저장한 유저 정보
     * @param inputMarcRequest 마크 아이디, 지시기호 및 데이터
     * @author 이창윤
     */
    @PostMapping("/marc/input/{userid}")
    public void inputMarc(@PathVariable("userid") Long userId,
                          @RequestBody InputMarcRequest inputMarcRequest) {
        String combinedMarc = marcService.combine(inputMarcRequest);
        marcService.saveWorked(inputMarcRequest.getMarc_id(), userId, combinedMarc);
    }

    /**
     * @implNote 입력된 마크 데이터를 불러옴 - Input(입력 완료/수정 완료 데이터), Modify(수정 요청한 데이터)
     * @param userId
     * @param marcId
     * @author 이창윤
     * @return 파싱된 마크의 worked
     */
    @GetMapping("/marc/check/{userid}")
    public ParseOneResponse viewCheckOne(@PathVariable("userid") Long userId,
                                         @RequestParam(value = "marcid", required = false) Long marcId) {
        User user = userService.findUser(userId);
        if (user.getAuth() == Authority.Worker) {
            throw new NoAuthorityException("권한 없는 유저(작업자가 검수페이지에 접근)");
        }

        if (marcId == null) {
            return marcService.parseCheckedOne();
        } else {
            return marcService.findParseCheckedOne(marcId);
        }
    } // 검수 페이지에서 파싱한 Worked 데이터 하나 던져줌

    /**
     * @implNote 입력된 데이터에 대한 수정 요청, 프로세스 정보를 Input -> Modify 로 바꾸고 Checker 정보 추가
     * @param userId
     * @param inputMarcRequest
     * @author 이창윤
     */
    @PostMapping("/marc/modify/{userid}")
    public void ModifyRequestMarc(@PathVariable("userid") Long userId,
                          @RequestBody CheckMarcRequest inputMarcRequest) {
        User user = userService.findUser(userId);
        if (user.getAuth() == Authority.Worker) {
            throw new NoAuthorityException("권한 없는 유저(작업자가 검수페이지에 접근)");
        }
        marcService.updateModifyState(inputMarcRequest.getMarc_id(),
                userId, inputMarcRequest.getComment());
    } // 검수 페이지 수정 요청

    /**
     * @implNote 검수(수정)한 데이터를 재조합하여 저장, Check 프로세스 insert
     * @param userId
     * @param inputMarcRequest
     * @aythor 이창윤
     */
    @PostMapping("/marc/check/{userid}")
    public void checkMarc(@PathVariable("userid") Long userId,
                          @RequestBody CheckMarcRequest inputMarcRequest) {
        User user = userService.findUser(userId);
        if (user.getAuth() == Authority.Worker) {
            throw new NoAuthorityException("권한 없는 유저(작업자가 검수페이지에 접근)");
        }
        String combinedMarc = marcService.combine(inputMarcRequest);
        marcService.saveChecked(inputMarcRequest.getMarc_id(), userId,
                combinedMarc, inputMarcRequest.getComment());
    } // 검수 페이지 저장
}


