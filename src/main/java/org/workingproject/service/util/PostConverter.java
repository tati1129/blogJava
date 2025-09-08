package org.workingproject.service.util;

import org.springframework.stereotype.Component;
import org.workingproject.dto.PostRequestDto;
import org.workingproject.dto.PostResponseDto;
import org.workingproject.entity.Post;
import org.workingproject.entity.PostStatus;
import org.workingproject.entity.User;

import java.time.LocalDate;

@Component
public class PostConverter {

    public Post fromDto(PostRequestDto dto) {
        Post post = new Post();

        post.setPostTitle(dto.getPostTitle());
        post.setPostDescription(dto.getPostDescription());

        User user = new User();
        user.setId(dto.getUserId());
        post.setUser(user);
        if (dto.getLastUpdate() != null) {
            post.setLastUpdate(LocalDate.parse(dto.getLastUpdate()));
        }
        /*
        пример своего формата даты
        String dateStr = "04.09.2025";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate date = LocalDate.parse(dateStr, formatter);

         */

        post.setStatus(PostStatus.PRIVATE);

        return post;
    }

    public PostResponseDto toDto(Post post) {
        PostResponseDto dto = new PostResponseDto();
/*

  private Integer id;
    private String postTitle;
    private String postDescription;
    private Integer userName;
    private String lastUpdate;
    private String status;

 */
        dto.setId(post.getId());
        dto.setPostTitle(post.getPostTitle());
        dto.setPostDescription(post.getPostDescription());
        dto.setUserName(post.getUser().getUserName());
        dto.setLastUpdate(post.getLastUpdate().toString());
        dto.setStatus(post.getStatus().name());
        return dto;
    }
}
