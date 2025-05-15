package selfit.selfit.domain.user.repository;

import selfit.selfit.domain.user.dto.UserAccountDto;
import selfit.selfit.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // 이메일로 계정 아이디 찾기(아이디 찾기)
    Optional<User> findByEmail(String email);

    // 계정 아이디로 사용자 찾기
    Optional<User> findByAccountId(String accountId);

}
