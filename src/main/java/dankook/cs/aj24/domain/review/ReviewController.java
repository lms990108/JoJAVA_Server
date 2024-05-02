package dankook.cs.aj24.domain.review;

import dankook.cs.aj24.domain.review.ReviewDocument;
import dankook.cs.aj24.domain.review.reviewdtos.CreateReviewDTO;
import dankook.cs.aj24.domain.review.reviewdtos.UpdateReviewDTO;
import dankook.cs.aj24.domain.review.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
@Tag(name = "Review API", description = "리뷰 관련 API")
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    // 리뷰 추가
    @PostMapping
    @Operation(summary = "리뷰 추가", description = "새로운 리뷰을 등록합니다.")
    @ApiResponse(responseCode = "200", description = "리뷰 등록 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReviewDocument.class)))
    public ResponseEntity<ReviewDocument> addReview(@RequestBody CreateReviewDTO createReviewDTO) {
        ReviewDocument createdReview = reviewService.addReview(createReviewDTO);
        return ResponseEntity.ok(createdReview);
    }

    // 리뷰 수정
    @PutMapping("/{reviewId}")
    @Operation(summary = "리뷰 수정", description = "기존 리뷰을 수정합니다.")
    @ApiResponse(responseCode = "200", description = "리뷰 수정 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReviewDocument.class)))
    public ResponseEntity<ReviewDocument> updateReview(@PathVariable String reviewId, @RequestBody UpdateReviewDTO updateReviewDTO) {
        ReviewDocument updatedReview = reviewService.updateReview(reviewId, updateReviewDTO);
        return ResponseEntity.ok(updatedReview);
    }

    // 리뷰 삭제
    @DeleteMapping("/{reviewId}")
    @Operation(summary = "리뷰 삭제", description = "리뷰을 삭제합니다. (soft_delete)")
    @ApiResponse(responseCode = "200", description = "리뷰 삭제 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReviewDocument.class)))
    public ResponseEntity<ReviewDocument> deleteReview(@PathVariable String reviewId){
        ReviewDocument deletedReview = reviewService.deleteReview(reviewId);
        return ResponseEntity.ok(deletedReview);
    }

    // 리뷰 조회
    @GetMapping("/{reviewId}")
    @Operation(summary = "리뷰 조회", description = "리뷰을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "리뷰 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReviewDocument.class)))
    public ResponseEntity<ReviewDocument> getReview(@PathVariable String reviewId) {
        ReviewDocument review = reviewService.getReview(reviewId);
        return ResponseEntity.ok(review);
    }

    @GetMapping
    @Operation(summary = "모든 리뷰 조회", description = "등록된 모든 리뷰 목록을 반환합니다.")
    @ApiResponse(responseCode = "200", description = "리뷰 목록 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReviewDocument.class)))
    public Page<ReviewDocument> getAllReviews(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return reviewService.getAllReviews(page, size);
    }
}
