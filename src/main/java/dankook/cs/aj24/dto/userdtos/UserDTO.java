package dankook.cs.aj24.dto.userdtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Getter
public class UserDTO {
    private String id;
    private String name;
    private String kakaoId;
    private List<String> hart;
    private String profileImg;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
}
