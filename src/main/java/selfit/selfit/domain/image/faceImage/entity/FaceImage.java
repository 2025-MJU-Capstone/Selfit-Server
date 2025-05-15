package selfit.selfit.domain.image.faceImage.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import selfit.selfit.domain.body.entity.Body;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class FaceImage {

    @Id @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String filePath;

    @Column(nullable = false)
    private LocalDateTime uploadDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "body_id")
    private Body body;
}
