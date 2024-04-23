package dankook.cs.aj24.dto.review;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CreateReviewDTO {
    private String title;
    private String content;
    private String stars;
    private String imgUrl;
}
