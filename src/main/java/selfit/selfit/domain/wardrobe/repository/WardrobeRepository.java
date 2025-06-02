package selfit.selfit.domain.wardrobe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import selfit.selfit.domain.clothes.entity.Clothes;
import selfit.selfit.domain.user.entity.User;
import selfit.selfit.domain.wardrobe.entity.Wardrobe;

import java.util.List;
import java.util.Optional;

public interface WardrobeRepository extends JpaRepository<Wardrobe, Long> {
    List<Wardrobe> findByUserId(Long userId);
    Optional<Wardrobe> findByPath(String deletePath);
}
