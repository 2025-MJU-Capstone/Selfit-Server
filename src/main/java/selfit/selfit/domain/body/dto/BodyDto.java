package selfit.selfit.domain.body.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BodyDto {
    private String face;
    private String height;
    private String weight;
    private String waist;
    private String photo;

    @Builder
    public BodyDto(String face, String height, String weight, String waist, String photo) {
        this.face = face;
        this.height = height;
        this.weight = weight;
        this.waist = waist;
        this.photo = photo;
    }
}
