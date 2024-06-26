package dankook.cs.aj24.domain.comment.commentdtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CreateCommentDTO {
    private String postId; // postId 필드 추가
    private String title;
    private String content;
}
