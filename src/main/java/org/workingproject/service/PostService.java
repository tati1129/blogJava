package org.workingproject.service;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.workingproject.dto.GeneralResponse;
import org.workingproject.dto.PostRequestDto;
import org.workingproject.dto.PostResponseDto;
import org.workingproject.entity.Post;
import org.workingproject.entity.PostStatus;
import org.workingproject.entity.User;
import org.workingproject.repository.PostRepository;
import org.workingproject.service.util.PostConverter;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PostService {

    private final PostRepository repository;
    private final UserService service;
    private final PostConverter converter;

    public GeneralResponse<PostResponseDto> createPost(PostRequestDto requestDto) {
        Optional<User> postUserOptional = service.getUserByIdForPostService(requestDto.getUserId());
        if (postUserOptional.isEmpty()) {
            return new GeneralResponse<>(HttpStatus.NOT_FOUND, null, "Невозможно создать задачу для незарегистрированного пользователя. User with this id not found");
        }

        User postUser = postUserOptional.get();

        Post post = converter.fromDto(requestDto);
        post.setUser(postUser);

        LocalDate today = LocalDate.now();
        post.setCreateDate(today);
        post.setLastUpdate(today);
        post.setStatus(PostStatus.PUBLIC);

        Post savedPost = repository.save(post);

        return new GeneralResponse<>(HttpStatus.CREATED, converter.toDto(savedPost), "Newpost added successfully");

    }

    public GeneralResponse<PostResponseDto> deletePostById(Integer id) {
        Optional<Post> postByIdOPtional = repository.deletePostById(id);

        if (postByIdOPtional.isEmpty()) {
            return new GeneralResponse<>(HttpStatus.NOT_FOUND, null, "Post with this id " + id + " not found");
        } else {
            return new GeneralResponse<>(HttpStatus.OK, converter.toDto(postByIdOPtional.get()), "Post deleted successfully");
        }
    }

    public GeneralResponse<List<PostResponseDto>> getAllPostsAdmin() {
        List<Post> allPosts = repository.findAll();
        List<PostResponseDto> responses = allPosts.stream()
                .map(p -> converter.toDto(p))
                .toList();
        return new GeneralResponse<>(HttpStatus.OK, responses, "All posts (admin mode)");
    }

    public GeneralResponse<List<PostResponseDto>> getAllPostsUser(Integer id) {
        Optional<User> userByIdOptional = service.getUserByIdForPostService(id);

        if (userByIdOptional.isEmpty()) {
            return new GeneralResponse<>(HttpStatus.NOT_FOUND, null, "User with this id not found");

        } else {
            List<Post> allPostByUSer = repository.findPostByUser(userByIdOptional.get());

            List<PostResponseDto> response = allPostByUSer.stream()
                    .map(p -> converter.toDto(p)).toList();
            return new GeneralResponse<>(HttpStatus.OK, response, "All posts (user mode)");
        }
    }

    public GeneralResponse<PostResponseDto> getPostById(Integer postId) {
        Optional<Post> postByIdOPtional = repository.findPostById(postId);

        if (postByIdOPtional.isEmpty()) {
            return new GeneralResponse<>(HttpStatus.NOT_FOUND, null, "Post with this id not found");
        } else {
            return new GeneralResponse<>(HttpStatus.OK, converter.toDto(postByIdOPtional.get()), "Post found successfully");
        }
    }

    public GeneralResponse<List<PostResponseDto>> getPostByTaskNameContent(String postName) {

        List<Post> postsByTaskName = repository.findByPostNameContent(postName);

        List<PostResponseDto> response = postsByTaskName.stream()
                .map(task -> converter.toDto(task))
                .toList();

        return new GeneralResponse<>(HttpStatus.OK, response, "All posts by post name successfully");
    }

}

