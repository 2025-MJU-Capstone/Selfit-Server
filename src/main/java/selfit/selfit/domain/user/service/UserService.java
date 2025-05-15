package selfit.selfit.domain.user.service;

import org.springframework.stereotype.Service;
import selfit.selfit.domain.user.dto.UserAccountDto;
import selfit.selfit.domain.user.dto.UserDetailDto;
import selfit.selfit.domain.user.dto.UserLoginRequestDto;
import selfit.selfit.domain.user.entity.User;

import java.util.List;
import java.util.Optional;

@Service
public interface UserService {
    User registerUser(UserAccountDto userAccountDto); // 계정 생성
    User updateUserDetails(UserDetailDto userDetailDto, String accountId); // 개인정보 등록
    String findAccountId(String email);
    String recoverPassword(String accountId, String email);
    void resetPassword(String accountId, String temporaryPassword, String email);
    User findUser(String accountId);
}
