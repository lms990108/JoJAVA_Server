package dankook.cs.aj24.dto.reviewdtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CreateReviewDTO {
    private String target;
    private String title;
    private String content;
    private String stars;
    private String imgUrl;
}
