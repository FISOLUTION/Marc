package fis.marc.controller;

import fis.marc.domain.User;
import fis.marc.dto.CreateUserRequest;
import fis.marc.dto.SearchWorkerResponse;
import fis.marc.dto.StatisticsResponse;
import fis.marc.dto.UpdateUserRequest;
import fis.marc.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/user/worker")
    public void createUser(@RequestBody CreateUserRequest ur) {
        User user = User.createUser(ur.getNickname(), ur.getPwd(), ur.getAuth(),
                ur.getUsername(), ur.getAddress(), ur.getPhnum(), LocalDate.now().toString());
        userService.createUser(user);
    } // 작업자 추가

    @GetMapping("/user/worker")
    public List<SearchWorkerResponse> searchAllWorker() {
        return userService.searchAllWorker();
    }
    // 작업자 조회

    @PatchMapping("/user/worker")
    public void updateWorker(@RequestBody UpdateUserRequest updateUserRequest) {
        userService.updateWorker(updateUserRequest);
    } // 작업자 수정

    @DeleteMapping("user/worker")
    public void deleteWorker(@RequestParam(value = "userid") Long userId) {
        userService.deleteWorker(userId);
    }
    // 작업자 삭제

    @GetMapping("/user/checker")
    public List<SearchWorkerResponse> searchAllChecker() {
        return userService.searchAllChecker();
    }
    // 검수자 조회

    @GetMapping("/statistics")
    public StatisticsResponse searchStatistics() {
        return userService.findAllWorkerByDate();
    }
    // 통계 자료

}
