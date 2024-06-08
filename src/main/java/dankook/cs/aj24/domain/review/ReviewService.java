package dankook.cs.aj24.domain.review;

import dankook.cs.aj24.common.error.CustomException;
import dankook.cs.aj24.domain.place.PlaceDocument;
import dankook.cs.aj24.domain.place.PlaceRepository;
import dankook.cs.aj24.domain.review.reviewdtos.UpdateReviewDTO;
import dankook.cs.aj24.domain.review.reviewdtos.CreateReviewDTO;
import dankook.cs.aj24.domain.user.UserDocument;
import dankook.cs.aj24.domain.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static dankook.cs.aj24.common.error.ErrorCode.*;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserService userService;
    private final PlaceRepository placeRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, UserService userService, PlaceRepository placeRepository) {
        this.reviewRepository = reviewRepository;
        this.userService = userService;
        this.placeRepository = placeRepository;
    }

    // 리뷰 생성
    public ReviewDocument addReview(CreateReviewDTO createReviewDTO) {
        // 현재 접속 중인 사용자 정보 조회
        UserDocument author = userService.getCurrentUser();

        // targetPlaceId로 PlaceDocument 조회
        PlaceDocument targetPlace = placeRepository.findByKakaoPlaceId(createReviewDTO.getTargetPlaceId())
                .orElseThrow(() -> new CustomException(OBJECT_NOT_FOUND));

        // createReviewDTO를 ReviewDocument로 변환
        ReviewDocument review = new ReviewDocument(
                null, // id는 자동 생성
                createReviewDTO.getTitle(),
                createReviewDTO.getContent(),
                createReviewDTO.getStars(),
                createReviewDTO.getImgUrl(),
                targetPlace, // PlaceDocument 설정
                author, // 작성자 정보 설정
                LocalDateTime.now(), // 생성일자 설정
                null, // 수정일자는 처음에는 null로 설정
                null  // 삭제일자는 처음에는 null로 설정
        );

        // 리뷰 추가
        ReviewDocument savedReview = reviewRepository.save(review);

        // PlaceDocument의 평점 업데이트
        updatePlaceRating(targetPlace);

        return savedReview;
    }

    // 리뷰 수정
    public ReviewDocument updateReview(String reviewId, UpdateReviewDTO updateReviewDTO) {
        // 현재 접속 중인 사용자 정보 조회
        UserDocument currentUser = userService.getCurrentUser();

        // 기존 리뷰 조회
        ReviewDocument existingReview = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(OBJECT_NOT_FOUND));

        // 현재 사용자가 작성자인지 확인
        if (!existingReview.getAuthor().getId().equals(currentUser.getId())) {
            throw new CustomException(USER_NOT_AUTHENTICATED);
        }

        // 리뷰 내용 업데이트
        existingReview.setTitle(updateReviewDTO.getTitle());
        existingReview.setContent(updateReviewDTO.getContent());
        existingReview.setStars(updateReviewDTO.getStars());
        existingReview.setImgUrl(updateReviewDTO.getImgUrl());

        // 수정일자 업데이트
        existingReview.setUpdatedAt(LocalDateTime.now());

        // 수정된 리뷰 저장
        ReviewDocument savedReview = reviewRepository.save(existingReview);

        // PlaceDocument의 평점 업데이트
        updatePlaceRating(existingReview.getTargetPlace());

        return savedReview;
    }

    // 리뷰 삭제
    public ReviewDocument deleteReview(String reviewId) {
        // 현재 접속 중인 사용자 정보 조회
        UserDocument currentUser = userService.getCurrentUser();

        // 기존 리뷰 조회
        ReviewDocument existingReview = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(OBJECT_NOT_FOUND));

        // 현재 사용자가 작성자인지 확인
        if (!existingReview.getAuthor().getId().equals(currentUser.getId())) {
            throw new CustomException(USER_NOT_AUTHENTICATED);
        }

        // 삭제일자 업데이트
        existingReview.setDeletedAt(LocalDateTime.now());

        // 리뷰 저장
        ReviewDocument deletedReview = reviewRepository.save(existingReview);

        // PlaceDocument의 평점 업데이트
        updatePlaceRating(existingReview.getTargetPlace());

        return deletedReview;
    }

    // 리뷰 조회
    public ReviewDocument getReview(String reviewId) {
        // 기존 리뷰 조회
        ReviewDocument existingReview = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(OBJECT_NOT_FOUND));

        // 기존 리뷰이 삭제되었는지 확인
        if (existingReview.isDeleted()) {
            // 삭제된 리뷰이면 예외를 던지거나 다른 처리를 수행할 수 있습니다.
            throw new CustomException(REVIEW_DELETED);
        } else {
            // 삭제되지 않은 리뷰이면 해당 리뷰 반환
            return existingReview;
        }
    }

    // 삭제되지 않은 리뷰 전체 조회 및 페이지네이션
    public Page<ReviewDocument> getAllReviews(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return reviewRepository.findByDeletedAtIsNull(pageable);
    }

    // 특정 targetPlace에 대한 리뷰 조회 및 페이지네이션
    public Page<ReviewDocument> getReviewsByTargetPlace(String targetPlaceId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        PlaceDocument targetPlace = placeRepository.findByKakaoPlaceId(targetPlaceId)
                .orElseThrow(() -> new CustomException(OBJECT_NOT_FOUND));
        return reviewRepository.findByTargetPlaceAndDeletedAtIsNull(targetPlace, pageable);
    }

    // 특정 유저가 작성한 리뷰 조회 및 페이지네이션
    public Page<ReviewDocument> getReviewsByAuthor(int page, int size) {
        // 현재 접속 중인 사용자 정보 조회
        UserDocument currentUser = userService.getCurrentUser();
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return reviewRepository.findByAuthorAndDeletedAtIsNull(currentUser, pageable);
    }

    // PlaceDocument의 평점 업데이트 메서드
    private void updatePlaceRating(PlaceDocument place) {
        double averageRating = reviewRepository.findByTargetPlaceAndDeletedAtIsNull(place)
                .stream()
                .mapToDouble(ReviewDocument::getStars)  // 별점 필드를 double로 바로 사용
                .average()
                .orElse(0.0);
        place.setRating(averageRating);
        placeRepository.save(place);
    }

}
