package selfit.selfit.domain.fitted.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import selfit.selfit.domain.fitted.entity.FittedImage;

public interface FittedImageRepository extends JpaRepository<FittedImage, Integer> {
}
