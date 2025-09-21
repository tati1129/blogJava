package org.workingproject.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.workingproject.dto.GeneralResponse;
import org.workingproject.dto.PostRequestDto;
import org.workingproject.dto.PostResponseDto;
import org.workingproject.dto.StatusUpdateRequest;
import org.workingproject.service.PostService;

import java.util.List;

@RestController
@RequestMapping("api/posts")
@AllArgsConstructor
public class PostController {
    private final PostService service;

    //найти все посты users mode where Status == Public
    @GetMapping()
    public List<PostResponseDto> getAllPostsUserMode() {
        return service.getAllPostsUserMode();
    }

    //найти все посты users mode where Status == Public
    @GetMapping("/admin")
    public List<PostResponseDto> getAllPostsAdminMode() {
        return service.getAllPostsAdmin();
    }

    //Созд пост
    @PostMapping("/newPost")
    public PostResponseDto createPost(@RequestBody PostRequestDto request) {
        return service.createPost(request);
    }

    //удалить пост
   // @DeleteMapping("/{id}")
    //public PostResponseDto deletePost(@PathVariable Integer id) {
    //     return service.deletePostById(id);
    // }

//    @GetMapping("/allPosts")

    //все посты юзера
    @GetMapping("/user/{userId}")
    public List<PostResponseDto> getPostByUser(@PathVariable Integer userId) {
        return service.getAllPostsUser(userId);
    }

    //найти пост по id поста
    @GetMapping("/{id}")
    public PostResponseDto getPostById(@PathVariable Integer id) {
        return service.getPostById(id);
    }

    //найти все посты, содержащие в названии контента
    @GetMapping("/contex")
    public List<PostResponseDto> getAllPostByContext(@RequestParam String searchText) {
        return service.getPostByTaskNameContent(searchText);
    }

    //обновить статус поста
    @PostMapping("/{taskId}/status")
    public String updatePostStatus(@PathVariable Integer taskId, @RequestBody StatusUpdateRequest dto) {
        return service.updatePostStatus(taskId, dto);
    }
}
