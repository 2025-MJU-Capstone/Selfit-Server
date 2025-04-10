package selfit.selfit.domain.user.service.Impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import selfit.selfit.domain.user.dto.UserAccountDto;
import selfit.selfit.domain.user.dto.UserDetailDto;
import selfit.selfit.domain.user.entity.User;
import selfit.selfit.domain.user.repository.UserRepository;
import selfit.selfit.domain.user.service.UserService;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원 가입(계정 생성)
     */
    @Override
    public User registerUser(UserAccountDto userAccountDto) {
        if(userAccountDto == null){
            throw new IllegalStateException("회원가입 정보가 필요합니다.");
        }

        // 중복 계정 검사
        validateDuplicateUser(userAccountDto.getAccountId());

        User user = User.builder()
                .accountId(userAccountDto.getAccountId())
                .password(passwordEncoder.encode(userAccountDto.getPassword()))
                .email(userAccountDto.getEmail())
                .build();

        return userRepository.save(user);
    }

    /**
     * 개인정보 등록
     */
    @Override
    public User updateUserDetails(Long userId, UserDetailDto userDetailDto){
        if(userDetailDto == null){
            throw new IllegalArgumentException("개인정보 등록 정보가 필요합니다.");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("계정이 없습니다."));

        user.setName(userDetailDto.getName());
        user.setAge(userDetailDto.getAge());
        user.setNickname(userDetailDto.getNickname());
        user.setGender(userDetailDto.getGender());
        user.setUpdate_date(LocalDateTime.now());

        return userRepository.save(user);
    }
    // 중복 계정 검사
    private void validateDuplicateUser(String accountId) {
//        Optional<User> findUser = userRepository.findById(user.getId());
//        if(findUser.isPresent()){ // user와 같은 값의 Id를 가진 객체가 있으면
//            throw new IllegalStateException("이미 존재하는 계정입니다.");
//        }
        boolean exists = userRepository.existsByAccountId(accountId);
        if(exists){
            throw new IllegalArgumentException("이미 존재하는 계정 ID입니다.");
        }
    }

    /**
     * 전체 회원 조회
     */
    public List<User> findAllUsers(){
        return userRepository.findAll();
    }

    /**
     * 회원 조회
     */
    public Optional<User> findOne(Long userId){
        return userRepository.findById(userId);
    }

    /**
     * 아이디 찾기(일단 이름과 이메일로)
     */
    public String findAccountId(String name, String email){
        return userRepository.findByNameAndEmail(name, email).getAccountId();
    }

    /**
     * 비밀번호 찾기(일단 계정 아이디와 이메일로)
     */

    public String findPassword(String AccountId, String email){

        return userRepository.findByAccountIdAndEmail(AccountId, email).getPassword();
    }


    /**
     * 카카오 로그인
     */


}
