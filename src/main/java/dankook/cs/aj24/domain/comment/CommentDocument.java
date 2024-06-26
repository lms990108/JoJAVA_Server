package dankook.cs.aj24.domain.comment;

import dankook.cs.aj24.domain.post.PostDocument;
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
@Document("comment")
public class CommentDocument {

    @Id
    private String id;
    private String title;
    private String content;

    @DBRef
    private PostDocument post;

    @DBRef
    private UserDocument author;

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
