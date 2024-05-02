package dankook.cs.aj24.domain.comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CommentRepository extends MongoRepository<CommentDocument, String> {
    Page<CommentDocument> findByDeletedAtIsNull(Pageable pageable);

}