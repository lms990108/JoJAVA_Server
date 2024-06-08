package dankook.cs.aj24.domain.review;

import dankook.cs.aj24.domain.place.PlaceDocument;
import dankook.cs.aj24.domain.user.UserDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ReviewRepository extends MongoRepository<ReviewDocument, String> {
    Page<ReviewDocument> findByDeletedAtIsNull(Pageable pageable);

    Page<ReviewDocument> findByTargetPlaceAndDeletedAtIsNull(PlaceDocument targetPlace, Pageable pageable);

    List<ReviewDocument> findByTargetPlaceAndDeletedAtIsNull(PlaceDocument targetPlace);

    Page<ReviewDocument> findByAuthorAndDeletedAtIsNull(UserDocument userDocument, Pageable pageable);
}
