package selfit.selfit.domain.fitted.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FittedImageDto {
    private String fitted_url;

    @Builder
    public FittedImageDto(String fitted_url) {
        this.fitted_url = fitted_url;
    }
}
