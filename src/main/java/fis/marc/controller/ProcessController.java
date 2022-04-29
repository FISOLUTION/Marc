package fis.marc.controller;

import fis.marc.dto.ProcessStatusResponse;
import fis.marc.dto.WorkListResponse;
import fis.marc.service.ProcessService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProcessController {
    private final ProcessService processService;

    @GetMapping("/marc/input/worklist/{userid}")
    public WorkListResponse viewWorkList(@PathVariable("userid") Long userId) throws IllegalAccessException {
        return processService.findAllByUserId(userId);
    } // 입력 페이지 작업 목록 조회

    @GetMapping("/process")
    public ProcessStatusResponse processStatusResponse() {
        return processService.findAllProcessStatus();
    } // 작업 현황 조회
}
