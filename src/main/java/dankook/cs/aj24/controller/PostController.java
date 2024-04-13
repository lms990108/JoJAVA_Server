package dankook.cs.aj24.controller;

import dankook.cs.aj24.document.PostDocument;
import dankook.cs.aj24.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public List<PostDocument> getAllPosts() {
        return postService.getAllPosts();
    }

    // 여기에 추가적인 CRUD API를 추가할 수 있음
}
