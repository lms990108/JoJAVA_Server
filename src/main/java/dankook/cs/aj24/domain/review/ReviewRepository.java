package dankook.cs.aj24.domain.review;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReviewRepository extends MongoRepository<ReviewDocument, String> {
    Page<ReviewDocument> findByDeletedAtIsNull(Pageable pageable);
    Page<ReviewDocument> findByTargetAndDeletedAtIsNull(String target, Pageable pageable);
}
