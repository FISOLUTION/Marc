package fis.marc.service;

import fis.marc.domain.User;
import fis.marc.dto.LoginRequest;
import fis.marc.dto.LoginResponse;
import fis.marc.exception.UserException;
import fis.marc.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;

    public User login(LoginRequest loginRequest){
        User user = userRepository.findOneByNickname(loginRequest.getNickname())
                .orElseThrow(() -> new UserException("존재하지 않는 id입니다."));
        if (user.getPwd().equals(loginRequest.getPassword())) {
            return user;
        }
        return null;
    }

    public LoginResponse userInfo(Long userId) {
        User user = userRepository.findOne(userId)
                .orElseThrow(() -> new UserException("해당 유저가 존재하지 않습니다"));
        if (user == null) {
            throw new IllegalStateException("잘못된 세션 정보");
        }
        return new LoginResponse(user.getAuth(), user.getUsername(), user.getId());
    }
}
