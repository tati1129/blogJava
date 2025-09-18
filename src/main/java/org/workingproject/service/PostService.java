package org.workingproject.service;

import lombok.AllArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.workingproject.dto.GeneralResponse;
import org.workingproject.dto.PostRequestDto;
import org.workingproject.dto.PostResponseDto;
import org.workingproject.dto.StatusUpdateRequest;
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
        //postUser.getPosts().add(post);//в список постов добавляем новый пост
        postUser.addPost(savedPost);//с помощью хелпера из entity User

        return new GeneralResponse<>(HttpStatus.CREATED, converter.toDto(savedPost), "Newpost added successfully");

    }


    public GeneralResponse<String> updatePostStatus(Integer id, StatusUpdateRequest request) {
        Optional<Post> postByIdOptional = repository.findById(id);
        if (postByIdOptional.isEmpty()) {
            return new GeneralResponse<>(HttpStatus.NOT_FOUND, null, "Post with this id " + id + " not found");
        } else {
            PostStatus newStatus = PostStatus.valueOf(request.getStatus());
            Post post = postByIdOptional.get();
            post.setStatus(newStatus);
            repository.save(post);
            return new GeneralResponse<>(HttpStatus.OK, "Status was  successfully changed", "Post deleted successfully");
        }

    }

//
//    public PostResponseDto deletePostById(Integer id) {
//        Optional<Post> postByIdOptional = repository.deletePostById(id);
//
//        if (postByIdOptional.isEmpty()) {
//            throw new ChangeSetPersister.NotFoundException("Post with this id " + id + " not found");
//        } else {
//            return converter.toDto(postByIdOptional.get());
//        }
//    }

    public GeneralResponse<List<PostResponseDto>> getAllPostsAdmin() {
        List<Post> allPosts = repository.findAll();
        List<PostResponseDto> responses = converter.toDtos(allPosts);
        return new GeneralResponse<>(HttpStatus.OK, responses, "All posts (admin mode)");
    }

    public GeneralResponse<List<PostResponseDto>> getAllPostsUserMode() {
        List<Post> allPosts = repository.findAll().stream()
                .filter(p -> p.getStatus() == PostStatus.PUBLIC)
                .toList();
        List<PostResponseDto> responses = converter.toDtos(allPosts);
        return new GeneralResponse<>(HttpStatus.OK, responses, "All posts (user mode)");
    }

    public GeneralResponse<List<PostResponseDto>> getAllPostsUser(Integer id) {
        Optional<User> userByIdOptional = service.getUserByIdForPostService(id);

        if (userByIdOptional.isEmpty()) {
            return new GeneralResponse<>(HttpStatus.NOT_FOUND, null, "User with this id not found");

        } else {
            List<Post> allPostByUSer = repository.findPostByUser(userByIdOptional.get());

            List<PostResponseDto> response = converter.toDtos(allPostByUSer);
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

        List<Post> postsByTaskName = repository.findByPostTitleContainingIgnoreCase(postName);

        List<PostResponseDto> response = postsByTaskName.stream().map(task -> converter.toDto(task)).toList();

        return new GeneralResponse<>(HttpStatus.OK, response, "All posts including in name " + postName);
    }

}

