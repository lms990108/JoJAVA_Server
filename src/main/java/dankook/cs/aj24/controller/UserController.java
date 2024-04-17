package dankook.cs.aj24.controller;

import dankook.cs.aj24.document.UserDocument;
import dankook.cs.aj24.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User API", description = "사용자 관련 API")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 사용자 추가
    @PostMapping
    @Operation(summary = "사용자 추가", description = "새로운 사용자를 등록합니다.")
    @ApiResponse(responseCode = "200", description = "사용자 등록 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDocument.class)))
    public ResponseEntity<UserDocument> addUser(@RequestBody UserDocument userDocument) {
        UserDocument createdUser = userService.createUser(userDocument);
        return ResponseEntity.ok(createdUser);
    }

    // 사용자 수정
//    @PutMapping("/{userId}")
//    @Operation(summary = "사용자 수정", description = "기존 사용자 정보를 수정합니다.")
//    @ApiResponse(responseCode = "200", description = "사용자 수정 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDocument.class)))
//    public ResponseEntity<UserDocument> updateUser(@PathVariable String userId, @RequestBody UserDocument userDocument) {
//        UserDocument updatedUser = userService.updateUser(userId, userDocument);
//        return ResponseEntity.ok(updatedUser);
//    }

    // 사용자 삭제
    @DeleteMapping("/{userId}")
    @Operation(summary = "사용자 삭제", description = "사용자 정보를 삭제합니다. (soft_delete)")
    @ApiResponse(responseCode = "200", description = "사용자 삭제 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDocument.class)))
    public ResponseEntity<Void> deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }

    // 특정 사용자 조회
    @GetMapping("/{userId}")
    @Operation(summary = "사용자 조회", description = "특정 사용자 정보를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "사용자 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDocument.class)))
    public ResponseEntity<UserDocument> getUser(@PathVariable String userId) {
        UserDocument user = userService.getUserById(userId).orElse(null);
        return ResponseEntity.ok(user);
    }

    // 모든 사용자 조회
    @GetMapping
    @Operation(summary = "모든 사용자 조회", description = "등록된 모든 사용자 목록을 반환합니다.")
    @ApiResponse(responseCode = "200", description = "사용자 목록 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = List.class)))
    public ResponseEntity<List<UserDocument>> getAllUsers() {
        List<UserDocument> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
}

