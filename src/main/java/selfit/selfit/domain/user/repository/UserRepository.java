package selfit.selfit.domain.user.repository;

import selfit.selfit.domain.user.dto.UserAccountDto;
import selfit.selfit.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByNameAndEmail(String name, String email);
    User findByAccountIdAndEmail(String accountId, String email);
    boolean existsByAccountId(String accountId);
}
