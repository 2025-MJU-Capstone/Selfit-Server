package selfit.selfit.domain.body.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import selfit.selfit.domain.body.entity.Body;
import selfit.selfit.domain.user.entity.User;

import java.util.Optional;

public interface BodyRepository extends JpaRepository<Body, Long> {
    Optional<Body> findByUser(User user);
}
