package dankook.cs.aj24.dto.reviewdtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class ReviewDTO {
    private Long id;
    private String target;
    private String title;
    private String content;
    private String stars;
    private String imgUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
}
