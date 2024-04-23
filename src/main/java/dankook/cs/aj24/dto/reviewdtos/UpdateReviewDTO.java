package dankook.cs.aj24.dto.reviewdtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
public class UpdateReviewDTO {
    private String title;
    private String content;
    private String stars;
    private String imgUrl;
}
