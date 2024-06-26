package dankook.cs.aj24.domain.post;

import dankook.cs.aj24.common.error.CustomException;
import dankook.cs.aj24.domain.post.postdtos.CreatePostDTO;
import dankook.cs.aj24.domain.post.postdtos.UpdatePostDTO;
import dankook.cs.aj24.domain.user.UserDocument;
import dankook.cs.aj24.domain.user.UserRepository;
import dankook.cs.aj24.domain.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;

import static dankook.cs.aj24.common.error.ErrorCode.*;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserService userService;

    @Autowired
    public PostService(PostRepository postRepository, UserService userService) {
        this.postRepository = postRepository;
        this.userService = userService;
    }

    // 게시글 생성
    public PostDocument addPost(CreatePostDTO createPostDTO) {
        // 현재 접속 중인 사용자 정보 조회
        UserDocument author = userService.getCurrentUser();

        // 이전에 등록된 게시글 수 조회하여 post_number 설정
        long postCount = postRepository.count();
        int postNumber = (int) (postCount == 0 ? 1 : postCount + 1);

        // CreatePostDTO를 PostDocument로 변환
        PostDocument post = new PostDocument(
                null, // id는 자동 생성
                postNumber,
                createPostDTO.getTitle(),
                createPostDTO.getContent(),
                createPostDTO.getImgUrl(),
                author, // 작성자 정보 설정
                LocalDateTime.now(), // 생성일자 설정
                null, // 수정일자는 처음에는 null로 설정
                null  // 삭제일자는 처음에는 null로 설정
        );

        // 게시글 추가
        return postRepository.save(post);
    }

    // 게시글 수정
    public PostDocument updatePost(String postId, UpdatePostDTO updatePostDTO) {
        // 현재 접속 중인 사용자 정보 조회
        UserDocument currentUser = userService.getCurrentUser();

        // 기존 게시글 조회
        PostDocument existingPost = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(OBJECT_NOT_FOUND));

        // 현재 사용자가 작성자인지 확인
        if (!existingPost.getAuthor().getId().equals(currentUser.getId())) {
            throw new CustomException(USER_NOT_AUTHENTICATED);
        }

        // 업데이트할 필드만 설정
        if (updatePostDTO.getTitle() != null) {
            existingPost.setTitle(updatePostDTO.getTitle());
        }
        if (updatePostDTO.getContent() != null) {
            existingPost.setContent(updatePostDTO.getContent());
        }
        if (updatePostDTO.getImgUrl() != null) {
            existingPost.setImgUrl(updatePostDTO.getImgUrl());
        }

        // 수정일자 업데이트
        existingPost.setUpdatedAt(LocalDateTime.now());

        // 수정된 게시글 저장
        return postRepository.save(existingPost);
    }


    // 게시글 삭제
    public PostDocument deletePost(String postId) {
        // 현재 접속 중인 사용자 정보 조회
        UserDocument currentUser = userService.getCurrentUser();

        // 기존 게시글 조회
        PostDocument existingPost = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(OBJECT_NOT_FOUND));

        // 현재 사용자가 작성자인지 확인
        if (!existingPost.getAuthor().getId().equals(currentUser.getId())) {
            throw new CustomException(USER_NOT_AUTHENTICATED);
        }

        // 삭제일자 업데이트
        existingPost.setDeletedAt(LocalDateTime.now());

        return postRepository.save(existingPost);
    }

    // 게시글 조회
    public PostDocument getPost(String postId) {
        // 기존 게시글 조회
        PostDocument existingPost = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(OBJECT_NOT_FOUND));

        // 기존 게시글이 삭제되었는지 확인
        if (existingPost.isDeleted()) {
            // 삭제된 게시글이면 예외를 던지거나 다른 처리를 수행할 수 있습니다.
            throw new CustomException(POST_DELETED);
        } else {
            // 삭제되지 않은 게시글이면 해당 게시글 반환
            return existingPost;
        }
    }

    // 삭제되지 않은 게시글 전체 조회 및 페이지네이션
    public Page<PostDocument> getAllPosts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return postRepository.findByDeletedAtIsNull(pageable);
    }
}
