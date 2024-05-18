package dankook.cs.aj24.domain.post.postdtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class PostDTO {
    private String id;
    private Integer postNumber;
    private String title;
    private String content;
    private String imgUrl;
    private String authorId; // 작성자 ID 추가
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
}
