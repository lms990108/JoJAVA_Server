
package dankook.cs.aj24.domain.user;

import dankook.cs.aj24.common.util.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@Builder
@Document("user")
public class UserDocument {

    @Id
    private String id;
    private String name;
    private String email;
    private String socialId; // 이 필드가 추가되어야 합니다.
    private List<String> hart; // 게시글 좋아요 배열로 사용하기 위해 List<String> 형으로 변경
    private String profileImg;
    private UserRole userRole;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    @Transient
    public boolean isDeleted() {
        return deletedAt != null;
    }
}
