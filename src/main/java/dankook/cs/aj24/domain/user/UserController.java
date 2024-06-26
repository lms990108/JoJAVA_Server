package dankook.cs.aj24.domain.user;

import dankook.cs.aj24.domain.review.ReviewDocument;
import dankook.cs.aj24.domain.review.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User API", description = "사용자 관련 API")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ReviewService reviewService;

    @GetMapping("get-user")
    @Operation(summary = "사용자 조회", description = "user Id를 통해 사용자 정보를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "사용자 정보 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDocument.class)))
    @ApiResponse(responseCode = "404", description = "해당하는 사용자 정보 없음", content = @Content(mediaType = "application/json"))
    public ResponseEntity<UserDocument> getUser(@RequestParam String userId) {
        UserDocument user = userService.getUser(userId);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/current")
    @Operation(summary = "현재 사용자 정보 조회", description = "현재 인증된 사용자의 정보를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "사용자 정보 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDocument.class)))
    @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "410", description = "탈퇴된 사용자", content = @Content(mediaType = "application/json"))
    public ResponseEntity<UserDocument> getCurrentUser() {
        UserDocument currentUser = userService.getCurrentUser();
        return ResponseEntity.ok(currentUser);
    }

    @GetMapping("/my-reviews")
    @Operation(summary = "현재 유저가 작성한 리뷰 조회", description = "현재 인증된 사용자가 작성한 리뷰 목록을 반환합니다.")
    @ApiResponse(responseCode = "200", description = "리뷰 목록 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReviewDocument.class)))
    @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자", content = @Content(mediaType = "application/json"))
    public Page<ReviewDocument> getReviewsByCurrentUser(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return reviewService.getReviewsByAuthor(page, size);
    }

    @PostMapping("/logout")
    @Operation(summary = "사용자 로그아웃", description = "사용자가 로그아웃하면, 해당 사용자의 토큰을 블랙리스트에 추가합니다.")
    @ApiResponse(responseCode = "200", description = "로그아웃 성공", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "410", description = "탈퇴된 사용자", content = @Content(mediaType = "application/json"))
    public ResponseEntity<String> logoutUser(@RequestHeader("Authorization") String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token format.");
        }

        String token = authorizationHeader.substring(7); // "Bearer " 다음 부분이 토큰
        userService.logout(token);
        return ResponseEntity.ok("Logged out successfully.");
    }

    @PostMapping("/favorite-places")
    @Operation(summary = "찜목록 추가", description = "현재 인증된 사용자의 찜목록에 장소를 추가합니다.")
    @ApiResponse(responseCode = "200", description = "찜목록 추가 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDocument.class)))
    @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "404", description = "사용자 찾을 수 없음", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "409", description = "중복된 리소스", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "410", description = "탈퇴된 사용자", content = @Content(mediaType = "application/json"))
    public ResponseEntity<UserDocument> addFavoritePlace(@RequestParam String kakaoPlaceId) {
        UserDocument updatedUser = userService.addFavoritePlace(kakaoPlaceId);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/favorite-places")
    @Operation(summary = "찜목록 삭제", description = "현재 인증된 사용자의 찜목록에서 장소를 삭제합니다.")
    @ApiResponse(responseCode = "200", description = "찜목록 삭제 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDocument.class)))
    @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "404", description = "사용자 찾을 수 없음", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "409", description = "중복된 리소스", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "410", description = "탈퇴된 사용자", content = @Content(mediaType = "application/json"))
    public ResponseEntity<UserDocument> removeFavoritePlace(@RequestParam String kakaoPlaceId) {
        UserDocument updatedUser = userService.removeFavoritePlace(kakaoPlaceId);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/delete")
    @Operation(summary = "회원 탈퇴", description = "사용자가 회원 탈퇴를 요청하면, deletedAt 필드에 현재 시간을 저장합니다.")
    @ApiResponse(responseCode = "200", description = "회원 탈퇴 성공", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "404", description = "사용자 찾을 수 없음", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "410", description = "탈퇴된 사용자", content = @Content(mediaType = "application/json"))
    public ResponseEntity<String> deleteUser() {
        UserDocument currentUser = userService.getCurrentUser();
        userService.deleteUser(currentUser.getId());
        return ResponseEntity.ok("User successfully deleted.");
    }
}
