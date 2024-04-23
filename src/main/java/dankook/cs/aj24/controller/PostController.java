package dankook.cs.aj24.controller;

import dankook.cs.aj24.document.PostDocument;
import dankook.cs.aj24.dto.post.CreatePostDTO;
import dankook.cs.aj24.service.PostService;
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
@RequestMapping("/api/posts")
@Tag(name = "Post API", description = "게시글 관련 API")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    // 게시글 추가
    @PostMapping
    @Operation(summary = "게시글 추가", description = "새로운 게시글을 등록합니다.")
    @ApiResponse(responseCode = "200", description = "게시글 등록 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PostDocument.class)))
    public ResponseEntity<PostDocument> addPost(@RequestBody CreatePostDTO createPostDTO) {
        PostDocument createdPost = postService.addPost(createPostDTO);
        return ResponseEntity.ok(createdPost);
    }

    @GetMapping
    @Operation(summary = "모든 게시글 조회", description = "등록된 모든 게시글 목록을 반환합니다.")
    @ApiResponse(responseCode = "200", description = "게시글 목록 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PostDocument.class)))
    public List<PostDocument> getAllPosts() {
        return postService.getAllPosts();
    }
}
