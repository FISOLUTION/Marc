package fis.marc.controller;

import fis.marc.domain.User;
import fis.marc.dto.CreateUserRequest;
import fis.marc.dto.SearchWorkerResponse;
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
    }

    @GetMapping("/user/worker")
    public List<SearchWorkerResponse> searchAllWorker() {
        return userService.searchAllWorker();
    }

    @PatchMapping("/user/worker")
    public void updateWorker(@RequestBody UpdateUserRequest updateUserRequest) {
        userService.updateWorker(updateUserRequest);
    }

    @DeleteMapping("user/worker")
    public void deleteWorker(@RequestParam(value = "userid") Long userId) {
        userService.deleteWorker(userId);
    }

    @GetMapping("/user/checker")
    public List<SearchWorkerResponse> searchAllChecker() {
        return userService.searchAllChecker();
    }

}

