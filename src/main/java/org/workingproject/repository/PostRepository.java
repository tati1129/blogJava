package org.workingproject.repository;

import org.workingproject.entity.Post;

import java.util.List;
import java.util.Optional;

public interface PostRepository {
    Post save(Post post);

    Post update(Post post);

    Post deletePostById(Integer id);

    List<Post> findAll();

    Optional<Post> findPostById(Integer id);

    Optional<Post> findPostByTitle(String name);

    Optional<Post> changeStatus(Post post);
}
