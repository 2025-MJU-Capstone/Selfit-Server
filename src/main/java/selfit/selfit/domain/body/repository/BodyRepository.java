package selfit.selfit.domain.body.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import selfit.selfit.domain.body.entity.Body;

public interface BodyRepository extends JpaRepository<Body, Long> {
}
