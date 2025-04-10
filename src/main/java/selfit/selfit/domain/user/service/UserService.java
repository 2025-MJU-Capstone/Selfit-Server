package selfit.selfit.domain.user.service;

import org.springframework.stereotype.Service;
import selfit.selfit.domain.user.dto.UserAccountDto;
import selfit.selfit.domain.user.dto.UserDetailDto;
import selfit.selfit.domain.user.entity.User;

import java.util.Optional;

@Service
public interface UserService {

    User registerUser(UserAccountDto userAccountDto); // 계정 생성
    User updateUserDetails(Long userId, UserDetailDto userDetailDto); // 개인정보 등록
}
