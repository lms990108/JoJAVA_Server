package dankook.cs.aj24.dto.commentdtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UpdateCommentDTO {
    private String title;
    private String content;
}
