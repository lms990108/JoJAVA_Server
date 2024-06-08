package dankook.cs.aj24.domain.review.reviewdtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
public class UpdateReviewDTO {
    private String title;
    private String content;
    private double stars;
    private String imgUrl;
}
