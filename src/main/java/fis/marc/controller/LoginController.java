package fis.marc.controller;

import fis.marc.configurator.Login;
import fis.marc.domain.User;
import fis.marc.dto.LoginRequest;
import fis.marc.dto.LoginResponse;
import fis.marc.service.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
@Slf4j
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response) {

        // user nickname pwd 유효성 검사
        User user = loginService.login(loginRequest);
        if (user == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }

        HttpSession session = request.getSession();
        session.setAttribute("loginId", user.getId());
        return new LoginResponse(user.getAuth(), user.getUsername(), user.getId());
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

    @GetMapping("/userinfo")
    public LoginResponse userInfo(@Login Long userId) {
        return loginService.userInfo(userId);
    }
}
