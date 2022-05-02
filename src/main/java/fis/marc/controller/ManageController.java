package fis.marc.controller;

import fis.marc.dto.GoalRequest;
import fis.marc.service.ManageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ManageController {
    private final ManageService manageService;

    @PostMapping("/goal")
    public void saveGoal(@RequestBody GoalRequest goalRequest) {
        manageService.saveGoal(goalRequest.getGoal());
    }
}
