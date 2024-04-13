package dankook.cs.aj24.service;

import dankook.cs.aj24.document.PostDocument;
import dankook.cs.aj24.dto.CreatePostDTO;
import dankook.cs.aj24.dto.UpdatePostDTO;
import dankook.cs.aj24.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    // 게시글 생성
    public PostDocument addPost(CreatePostDTO createPostDTO) {
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
                LocalDateTime.now(), // 생성일자 설정
                null, // 수정일자는 처음에는 null로 설정
                null  // 삭제일자는 처음에는 null로 설정
        );

        // 게시글 추가
        return postRepository.save(post);
    }

    // 게시글 수정
    public PostDocument updatePost(String postId, UpdatePostDTO updatePostDTO) {
        // 기존 게시글 조회
        PostDocument existingPost = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다: " + postId));

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


    // 전체 조회
    public List<PostDocument> getAllPosts() {
        return postRepository.findAll();
    }
}
