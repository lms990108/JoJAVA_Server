package dankook.cs.aj24.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import dankook.cs.aj24.document.UserDocument;

import java.lang.reflect.Member;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<UserDocument, String> {
    Optional<UserDocument> findBySocialId(String socialId);
}