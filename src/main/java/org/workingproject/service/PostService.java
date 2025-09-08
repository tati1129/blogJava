package org.workingproject.service;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.workingproject.dto.GeneralResponce;
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

    public GeneralResponce<PostResponseDto> createPost(PostRequestDto requestDto) {
        Optional<User> postUserOptional = service.getUserByIdForPostService(requestDto.getUserId());
        if (postUserOptional.isEmpty()) {
            return new GeneralResponce<>(HttpStatus.NOT_FOUND, null, "Невозможно создать задачу для незарегистрированного пользователя. User with this id not found");
        }

        User postUser = postUserOptional.get();

        Post post = converter.fromDto(requestDto);
        post.setUser(postUser);

        LocalDate today = LocalDate.now();
        post.setCreateDate(today);
        post.setLastUpdate(today);
        post.setStatus(PostStatus.PUBLIC);

        Post savedPost = repository.save(post);

        return new GeneralResponce<>(HttpStatus.CREATED, converter.toDto(savedPost), "Newpost added successfully");

    }

    public GeneralResponce<PostResponseDto> deletePostById(Integer id) {
        Optional<Post> postByIdOPtional = repository.deletePostById(id);

        if (postByIdOPtional.isEmpty()) {
            return new GeneralResponce<>(HttpStatus.NOT_FOUND, null, "Post with this id " + id + " not found");
        }else {
            return  new GeneralResponce<>(HttpStatus.OK,converter.toDto(postByIdOPtional.get()), "Post deleted successfully");
        }
    }

    public GeneralResponce<List<PostResponseDto>>   getAllPostsAdmin() {
        List<Post> allPosts = repository.findAll();
        List<PostResponseDto> responses = allPosts.stream()
                .map(p -> converter.toDto(p))
                .toList();
        return new GeneralResponce<>(HttpStatus.OK, responses, "All posts (admin mode)");
    }

    public GeneralResponce<List<PostResponseDto>>   getAllPostsUser(Integer id){
        Optional<User> userByIdOptional = service.getUserByIdForPostService(id);

        if (userByIdOptional.isEmpty()) {
            return new GeneralResponce<>(HttpStatus.NOT_FOUND, null,"User with this id not found");

        }else {
            List<Post> allPostByUSer = repository.findPostByUser(userByIdOptional.get());

            List<PostResponseDto> response = allPostByUSer.stream()
                    .map(p -> converter.toDto(p)).toList();
            return new GeneralResponce<>(HttpStatus.OK, response,"All posts (user mode)");
        }
    }

    public GeneralResponce<PostResponseDto> getPostById(Integer postId) {
        Optional<Post> postByIdOPtional = repository.findPostById(postId);

        if (postByIdOPtional.isEmpty()) {
            return new GeneralResponce<>(HttpStatus.NOT_FOUND, null, "Post with this id not found");
        }else {
            return new GeneralResponce<>(HttpStatus.OK, converter.toDto(postByIdOPtional.get()), "Post found successfully");
        }
    }

    public GeneralResponce<List<PostResponseDto>> getTasksByTaskNameContent(String postName){

        List<Post>postsByTaskName = repository.findByPostNameContent(postName);

        List<PostResponseDto> response = postsByTaskName.stream()
                .map(task -> converter.toDto(task))
                .toList();

        return new GeneralResponce<>(HttpStatus.OK, response,"All posts by post name successfully");
    }


}

