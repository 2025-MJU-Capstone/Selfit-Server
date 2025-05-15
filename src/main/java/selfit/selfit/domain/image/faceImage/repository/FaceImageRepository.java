package selfit.selfit.domain.image.faceImage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import selfit.selfit.domain.image.faceImage.entity.FaceImage;

public interface FaceImageRepository extends JpaRepository<FaceImage, Long> {

}
