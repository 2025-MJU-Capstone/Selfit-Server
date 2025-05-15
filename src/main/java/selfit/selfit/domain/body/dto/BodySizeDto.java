package selfit.selfit.domain.body.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
public class BodySizeDto {
    // 체형 등록 시 필요한 DTo
    private String height;
    private String weight;
    private String waist;

    @Builder
    public BodySizeDto(String height, String weight, String waist) {
        this.height = height;
        this.weight = weight;
        this.waist = waist;
    }
}
