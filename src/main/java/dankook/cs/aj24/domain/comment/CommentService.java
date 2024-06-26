package dankook.cs.aj24.domain.comment;

import dankook.cs.aj24.common.error.CustomException;
import dankook.cs.aj24.domain.comment.commentdtos.CreateCommentDTO;
import dankook.cs.aj24.domain.comment.commentdtos.UpdateCommentDTO;
import dankook.cs.aj24.domain.post.PostDocument;
import dankook.cs.aj24.domain.post.PostRepository;
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
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserService userService;

    @Autowired
    public CommentService(CommentRepository commentRepository, PostRepository postRepository, UserService userService) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userService = userService;
    }

    // 댓글 생성
    public CommentDocument addComment(CreateCommentDTO createCommentDTO) {
        // 현재 접속 중인 사용자 정보 조회
        UserDocument author = userService.getCurrentUser();

        // 댓글이 작성될 게시글 조회
        PostDocument post = postRepository.findById(createCommentDTO.getPostId())
                .orElseThrow(() -> new CustomException(OBJECT_NOT_FOUND));

        // CreateCommentDTO를 CommentDocument로 변환
        CommentDocument comment = new CommentDocument(
                null, // id는 자동 생성
                createCommentDTO.getTitle(),
                createCommentDTO.getContent(),
                post, // 댓글이 작성된 게시글 설정
                author, // 작성자 정보 설정
                LocalDateTime.now(), // 생성일자 설정
                null, // 수정일자는 처음에는 null로 설정
                null  // 삭제일자는 처음에는 null로 설정
        );

        // 댓글 추가
        return commentRepository.save(comment);
    }

    // 댓글 수정
    public CommentDocument updateComment(String commentId, UpdateCommentDTO updateCommentDTO) {
        // 현재 접속 중인 사용자 정보 조회
        UserDocument currentUser = userService.getCurrentUser();

        // 기존 댓글 조회
        CommentDocument existingComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(OBJECT_NOT_FOUND));

        // 현재 사용자가 작성자인지 확인
        if (!existingComment.getAuthor().getId().equals(currentUser.getId())) {
            throw new CustomException(USER_NOT_AUTHENTICATED);
        }

        // 업데이트할 필드만 설정
        if (updateCommentDTO.getTitle() != null) {
            existingComment.setTitle(updateCommentDTO.getTitle());
        }
        if (updateCommentDTO.getContent() != null) {
            existingComment.setContent(updateCommentDTO.getContent());
        }

        // 수정일자 업데이트
        existingComment.setUpdatedAt(LocalDateTime.now());

        // 수정된 댓글 저장
        return commentRepository.save(existingComment);
    }

    // 댓글 삭제
    public CommentDocument deleteComment(String commentId) {
        // 현재 접속 중인 사용자 정보 조회
        UserDocument currentUser = userService.getCurrentUser();

        // 기존 댓글 조회
        CommentDocument existingComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(OBJECT_NOT_FOUND));

        // 현재 사용자가 작성자인지 확인
        if (!existingComment.getAuthor().getId().equals(currentUser.getId())) {
            throw new CustomException(USER_NOT_AUTHENTICATED);
        }

        // 삭제일자 업데이트
        existingComment.setDeletedAt(LocalDateTime.now());

        return commentRepository.save(existingComment);
    }

    // 댓글 조회
    public CommentDocument getComment(String commentId) {
        // 기존 댓글 조회
        CommentDocument existingComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(OBJECT_NOT_FOUND));

        // 기존 댓글이 삭제되었는지 확인
        if (existingComment.isDeleted()) {
            // 삭제된 댓글이면 예외를 던지거나 다른 처리를 수행할 수 있습니다.
            throw new CustomException(COMMENT_DELETED);
        } else {
            // 삭제되지 않은 댓글이면 해당 댓글 반환
            return existingComment;
        }
    }

    // 삭제되지 않은 댓글 전체 조회 및 페이지네이션
    public Page<CommentDocument> getAllComments(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return commentRepository.findByDeletedAtIsNull(pageable);
    }

    // 특정 게시글의 댓글 전체 조회 및 페이지네이션
    public Page<CommentDocument> getCommentsByPostId(String postId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return commentRepository.findByPostIdAndDeletedAtIsNull(postId, pageable);
    }
}
