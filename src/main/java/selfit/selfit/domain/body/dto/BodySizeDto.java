package selfit.selfit.domain.body.dto;

import lombok.*;

import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
public class BodySizeDto {
    // 체형 등록 시 필요한 DTo
    private String height;
    private String weight;
    private String waist;
    private String leg;
    private String shoulder;
    private String pelvis;
    private String chest;

}
