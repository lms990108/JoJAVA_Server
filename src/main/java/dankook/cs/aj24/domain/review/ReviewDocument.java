package dankook.cs.aj24.domain.review;

import dankook.cs.aj24.domain.place.PlaceDocument;
import dankook.cs.aj24.domain.user.UserDocument;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
@Document("review")
public class ReviewDocument {

    @Id
    private String id;
    private String title;
    private String content;
    private String stars;
    private String imgUrl;

    @DBRef
    private PlaceDocument targetPlace;

    @DBRef
    private UserDocument author; // 작성자 정보를 위한 DBRef 필드 추

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
