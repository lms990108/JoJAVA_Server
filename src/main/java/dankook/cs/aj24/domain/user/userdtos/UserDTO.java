package dankook.cs.aj24.domain.user.userdtos;

import dankook.cs.aj24.domain.user.UserRole;
import dankook.cs.aj24.domain.user.UserDocument;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Getter
public class UserDTO {
    private String id;
    private String name;
    private String email;
    private String socialId; // 이 필드가 추가되어야 합니다.
    private List<String> favorite_places;
    private String profileImg;
    private UserRole userRole;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
}
