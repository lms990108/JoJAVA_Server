package dankook.cs.aj24.service;

import dankook.cs.aj24.common.error.CustomException;
import dankook.cs.aj24.document.CommentDocument;
import dankook.cs.aj24.dto.commentdtos.CreateCommentDTO;
import dankook.cs.aj24.dto.commentdtos.UpdateCommentDTO;
import dankook.cs.aj24.repository.CommentRepository;
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

    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    // 댓글 생성
    public CommentDocument addComment(CreateCommentDTO createCommentDTO) {
        // 이전에 등록된 댓글 수 조회하여 comment_number 설정
        long commentCount = commentRepository.count();
        int commentNumber = (int) (commentCount == 0 ? 1 : commentCount + 1);

        // CreateCommentDTO를 CommentDocument로 변환
        CommentDocument comment = new CommentDocument(
                null, // id는 자동 생성
                createCommentDTO.getTitle(),
                createCommentDTO.getContent(),
                LocalDateTime.now(), // 생성일자 설정
                null, // 수정일자는 처음에는 null로 설정
                null  // 삭제일자는 처음에는 null로 설정
        );

        // 댓글 추가
        return commentRepository.save(comment);
    }

    // 댓글 수정
    public CommentDocument updateComment(String commentId, UpdateCommentDTO updateCommentDTO) {
        // 기존 댓글 조회
        CommentDocument existingComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(OBJECT_NOT_FOUND));

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
    public CommentDocument deleteComment(String commentId){
        // 기존 댓글 조회
        CommentDocument existingComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(OBJECT_NOT_FOUND));

        // 삭제일자 업데이트
        existingComment.setDeletedAt(LocalDateTime.now());

        return commentRepository.save(existingComment);
    }

    // 댓글 조회
    public CommentDocument getComment(String commentId){
        // 기존 댓글 조회
        CommentDocument existingComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(OBJECT_NOT_FOUND));

        // 기존 댓글이 삭제되었는지 확인
        if(existingComment.isDeleted()){
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
}
