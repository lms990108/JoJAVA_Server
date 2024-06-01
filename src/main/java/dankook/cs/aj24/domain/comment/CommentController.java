package dankook.cs.aj24.domain.comment;

import dankook.cs.aj24.domain.comment.CommentDocument;
import dankook.cs.aj24.domain.comment.commentdtos.CreateCommentDTO;
import dankook.cs.aj24.domain.comment.commentdtos.UpdateCommentDTO;
import dankook.cs.aj24.domain.comment.CommentService;
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
@RequestMapping("/api/comments")
@Tag(name = "Comment API", description = "댓글 관련 API")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    // 댓글 추가
    @PostMapping
    @Operation(summary = "댓글 추가", description = "새로운 댓글을 등록합니다.")
    @ApiResponse(responseCode = "200", description = "댓글 등록 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommentDocument.class)))
    @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "404", description = "사용자 찾을 수 없음", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "410", description = "탈퇴된 사용자", content = @Content(mediaType = "application/json"))
    public ResponseEntity<CommentDocument> addComment(@RequestBody CreateCommentDTO createCommentDTO) {
        CommentDocument createdComment = commentService.addComment(createCommentDTO);
        return ResponseEntity.ok(createdComment);
    }

    // 댓글 수정
    @PutMapping("/{commentId}")
    @Operation(summary = "댓글 수정", description = "기존 댓글을 수정합니다.")
    @ApiResponse(responseCode = "200", description = "댓글 수정 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommentDocument.class)))
    @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "404", description = "사용자 찾을 수 없음", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "410", description = "탈퇴된 사용자", content = @Content(mediaType = "application/json"))
    public ResponseEntity<CommentDocument> updateComment(@PathVariable String commentId, @RequestBody UpdateCommentDTO updateCommentDTO) {
        CommentDocument updatedComment = commentService.updateComment(commentId, updateCommentDTO);
        return ResponseEntity.ok(updatedComment);
    }

    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    @Operation(summary = "댓글 삭제", description = "댓글을 삭제합니다. (soft_delete)")
    @ApiResponse(responseCode = "200", description = "댓글 삭제 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommentDocument.class)))
    @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "404", description = "사용자 찾을 수 없음", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "410", description = "탈퇴된 사용자", content = @Content(mediaType = "application/json"))
    public ResponseEntity<CommentDocument> deleteComment(@PathVariable String commentId){
        CommentDocument deletedComment = commentService.deleteComment(commentId);
        return ResponseEntity.ok(deletedComment);
    }

    // 댓글 조회
    @GetMapping("/{commentId}")
    @Operation(summary = "댓글 조회", description = "댓글을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "댓글 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommentDocument.class)))
    public ResponseEntity<CommentDocument> getComment(@PathVariable String commentId) {
        CommentDocument comment = commentService.getComment(commentId);
        return ResponseEntity.ok(comment);
    }

    @GetMapping
    @Operation(summary = "모든 댓글 조회", description = "등록된 모든 댓글 목록을 반환합니다.")
    @ApiResponse(responseCode = "200", description = "댓글 목록 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommentDocument.class)))
    public Page<CommentDocument> getAllComments(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return commentService.getAllComments(page, size);
    }

    // 특정 게시글의 댓글 전체 조회
    @GetMapping("/post/{postId}")
    @Operation(summary = "특정 게시글의 댓글 조회", description = "특정 게시글에 달린 모든 댓글을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "댓글 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommentDocument.class)))
    public ResponseEntity<Page<CommentDocument>> getCommentsByPostId(@PathVariable String postId,
                                                                     @RequestParam(defaultValue = "0") int page,
                                                                     @RequestParam(defaultValue = "20") int size) {
        Page<CommentDocument> comments = commentService.getCommentsByPostId(postId, page, size);
        return ResponseEntity.ok(comments);
    }
}
