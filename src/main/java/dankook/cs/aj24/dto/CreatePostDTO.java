package dankook.cs.aj24.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CreatePostDTO {
    private String title;
    private String content;
    private String imgUrl;
}
