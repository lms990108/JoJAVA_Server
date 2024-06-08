package dankook.cs.aj24.domain.review.reviewdtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CreateReviewDTO {
    private String targetPlaceId; // PlaceDocument의 ID
    private String title;
    private String content;
    private String stars;
    private String imgUrl;
}
