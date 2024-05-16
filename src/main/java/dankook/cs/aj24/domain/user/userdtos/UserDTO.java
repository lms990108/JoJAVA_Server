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
    private List<String> hart;
    private String profileImg;
    private UserRole userRole;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public UserDocument toUserDocument() {
        return UserDocument.builder()
                .name(this.name)
                .email(this.email)
                .socialId(this.socialId)
                .hart(this.hart)
                .profileImg(this.profileImg)
                .userRole(UserRole.USER) // 기본 권한 설정
                .build();
    }
}
