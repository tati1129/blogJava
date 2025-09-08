package org.workingproject.repository;

import org.workingproject.entity.Post;
import org.workingproject.entity.User;

import java.util.List;
import java.util.Optional;

public interface PostRepository {
    Post save(Post post);

    Post update(Post post);

    Optional<Post> deletePostById(Integer id);

    List<Post> findAll();

    Optional<Post> findPostById(Integer id);

    List<Post> findPostByUser(User user);
    List<Post> findByPostNameContent(String postName);
    List<Post> findPostByTitle(String title);

    Optional<Post> changeStatus(Post post);
}
