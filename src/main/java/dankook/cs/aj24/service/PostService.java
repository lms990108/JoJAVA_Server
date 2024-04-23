package dankook.cs.aj24.service;

import dankook.cs.aj24.document.PostDocument;
import dankook.cs.aj24.dto.post.CreatePostDTO;
import dankook.cs.aj24.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    public PostDocument addPost(CreatePostDTO postDTO) {
        // 이전에 등록된 게시글 수 조회하여 post_number 설정
        long postCount = postRepository.count();
        int postNumber = (int) (postCount == 0 ? 0 : postCount + 1);

        // CreatePostDTO를 PostDocument로 변환
        PostDocument post = new PostDocument(
                null, // id는 자동 생성
                postNumber,
                postDTO.getTitle(),
                postDTO.getContent(),
                postDTO.getImgUrl(),
                LocalDateTime.now(), // 생성일자 설정
                null, // 수정일자는 처음에는 null로 설정
                null  // 삭제일자는 처음에는 null로 설정
        );

        // 게시글 추가
        return postRepository.save(post);
    }

    public List<PostDocument> getAllPosts() {
        return postRepository.findAll();
    }
}
