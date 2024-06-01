package dankook.cs.aj24.domain.user;

import dankook.cs.aj24.common.error.CustomException;
import dankook.cs.aj24.domain.oauth.PrincipalDetail;
import dankook.cs.aj24.domain.user.userdtos.UserDTO;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User API", description = "사용자 관련 API")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("get-user")
    @Operation(summary = "사용자 조회", description = "user Id를 통해 사용자 정보를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "사용자 정보 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDocument.class)))
    @ApiResponse(responseCode = "404", description = "해당하는 사용자 정보 없음", content = @Content(mediaType = "application/json"))
    public ResponseEntity<?> getUser(@RequestParam String userId){
        UserDocument user = userService.getUser(userId);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/current")
    @Operation(summary = "현재 사용자 정보 조회", description = "현재 인증된 사용자의 정보를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "사용자 정보 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDocument.class)))
    @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자", content = @Content(mediaType = "application/json"))
    public ResponseEntity<?> getCurrentUser() {
        try {
            UserDocument currentUser = userService.getCurrentUser();
            return ResponseEntity.ok(currentUser);
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping("/logout")
    @Operation(summary = "사용자 로그아웃", description = "사용자가 로그아웃하면, 해당 사용자의 토큰을 블랙리스트에 추가합니다.")
    @ApiResponse(responseCode = "200", description = "로그아웃 성공", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자", content = @Content(mediaType = "application/json"))
    public ResponseEntity<?> logoutUser(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token format.");
            }

            String token = authorizationHeader.substring(7); // "Bearer " 다음 부분이 토큰
            userService.logout(token);
            return ResponseEntity.ok("Logged out successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Logout failed: " + e.getMessage());
        }
    }

    @PostMapping("/hart")
    @Operation(summary = "찜목록 추가", description = "현재 인증된 사용자의 찜목록에 장소를 추가합니다.")
    @ApiResponse(responseCode = "200", description = "찜목록 추가 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDocument.class)))
    @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "404", description = "사용자 찾을 수 없음", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "409", description = "중복된 리소스", content = @Content(mediaType = "application/json"))
    public ResponseEntity<?> addHart(@RequestParam String placeId) {
        try {
            UserDocument updatedUser = userService.addHart(placeId);
            return ResponseEntity.ok(updatedUser);
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

}
