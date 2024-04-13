package dankook.cs.aj24.service;

import dankook.cs.aj24.document.PostDocument;
import dankook.cs.aj24.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<PostDocument> getAllPosts() {
        return postRepository.findAll();
    }

    // 여기에 추가적인 CRUD 메서드를 추가할 수 있음
}
