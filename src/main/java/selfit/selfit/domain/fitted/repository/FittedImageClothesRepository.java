package selfit.selfit.domain.fitted.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import selfit.selfit.domain.fitted.entity.FittedImageClothes;

public interface FittedImageClothesRepository extends JpaRepository<FittedImageClothes,Integer> {
}
