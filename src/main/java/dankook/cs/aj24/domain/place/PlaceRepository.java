package dankook.cs.aj24.domain.place;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

@Repository
public interface PlaceRepository extends MongoRepository<PlaceDocument, String> {
    Page<PlaceDocument> findByDeletedAtIsNull(Pageable pageable);

    Optional<PlaceDocument> findByKakaoPlaceId(String kakaoPlaceId);
}
