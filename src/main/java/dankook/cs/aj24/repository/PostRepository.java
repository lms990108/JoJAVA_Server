package dankook.cs.aj24.repository;

import dankook.cs.aj24.document.PostDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PostRepository extends MongoRepository<PostDocument, String> {
    // 여기에 필요한 사용자 정의 메서드 추가 가능
}
