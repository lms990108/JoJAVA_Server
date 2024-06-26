package dankook.cs.aj24.domain.post.postdtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UpdatePostDTO {
    private String title;
    private String content;
    private String imgUrl;
}
