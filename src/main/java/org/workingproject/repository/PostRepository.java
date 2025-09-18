package org.workingproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.workingproject.entity.Post;
import org.workingproject.entity.User;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Integer> {

    List<Post> findPostByUser(User user);

    List<Post> findByPostTitleContainingIgnoreCase(String postTitle);

    Optional<Post> findPostById(Integer postId);


}
