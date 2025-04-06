package selfit.selfit.domain.wardrobe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import selfit.selfit.domain.wardrobe.entity.Wardrobe;

public interface WardrobeRepository extends JpaRepository<Wardrobe, Long> {
}
