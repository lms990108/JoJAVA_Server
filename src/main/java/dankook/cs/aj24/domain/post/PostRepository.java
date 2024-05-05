package dankook.cs.aj24.domain.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface PostRepository extends MongoRepository<PostDocument, String> {
    Page<PostDocument> findByDeletedAtIsNull(Pageable pageable);
}