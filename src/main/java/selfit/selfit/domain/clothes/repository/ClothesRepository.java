package selfit.selfit.domain.clothes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import selfit.selfit.domain.clothes.entity.Clothes;

public interface ClothesRepository extends JpaRepository<Clothes,Long> {
}
