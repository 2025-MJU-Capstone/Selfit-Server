package selfit.selfit.domain.body.dto;


import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FaceFileDto {
    private String fileName;
    private String filePath;
}
