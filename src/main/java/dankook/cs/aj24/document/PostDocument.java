package dankook.cs.aj24.document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Document("post")
public class PostDocument {

    @Id
    private String id;
    private Integer postNumber;
    private String title;
    private String content;
    private String imgUrl;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt; // deleted_At 필드 추가

    @Transient
    public boolean isDeleted() {
        return deletedAt != null;
    }
}
