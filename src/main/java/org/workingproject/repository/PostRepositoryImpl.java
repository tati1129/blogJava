package org.workingproject.repository;

import org.springframework.stereotype.Repository;
import org.workingproject.entity.Post;
import org.workingproject.entity.PostStatus;
import org.workingproject.entity.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class PostRepositoryImpl implements PostRepository {
    private final List<Post> posts;
    private Integer postIdCounter;

    public PostRepositoryImpl() {
        this.posts = new ArrayList<>();
        this.postIdCounter = 0;
    }

    @Override
    public Post save(Post post) {
        if (post.getId() == null) {
            post.setId(++postIdCounter);
            posts.add(post);
        } else {
            //Если у поста id уже есть (значит, это обновление существующего поста)
            posts.removeIf(t -> t.getId().equals(post.getId()));
            posts.add(post);
        }
        return post;
    }

    @Override
    public Post update(Post post) {
        if (post.getId() == null) {
            return null;
        }
        for (int i = 0; i < posts.size(); i++) {
            Post p = posts.get(i);

            if (p.getId().equals(post.getId())) {
                post.setLastUpdate(LocalDate.now());
                posts.set(i, post);
                return post;
            }
        }
        return null; // если пост с таким id не найден
    }

    @Override
    public Optional<Post> deletePostById(Integer id) {
        Optional<Post> postForDeleteOptional = findPostById(id);
        if (postForDeleteOptional.isEmpty()) {
            return Optional.empty();
        } else {
            posts.remove(postForDeleteOptional.get());
            return postForDeleteOptional;
        }

    }

    @Override
    public List<Post> findAll() {
        return new ArrayList<>(posts);
    }

    @Override
    public Optional<Post> findPostById(Integer id) {
        return posts.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Post> findPostByUser(User user) {
        return posts.stream()
                .filter(p -> p.getUser().equals(user))
                .toList();
    }

    @Override
    public List<Post> findByPostNameContent(String postName) {
        return posts.stream()
                .filter(p -> p.getPostTitle().toLowerCase().contains(postName.toLowerCase()))
                .toList();
    }

    @Override
    public List<Post> findPostByTitle(String title) {
        return posts.stream()
                .filter(p -> p.getPostTitle().toLowerCase().contains(title.toLowerCase()))
                .toList();
    }

    @Override
    public Optional<Post> changeStatus(Post post) {
        if (post == null) {
            return Optional.empty();
        }
        if (post.getStatus() == PostStatus.PRIVATE) {
            post.setStatus(PostStatus.PUBLIC);
        } else {
            post.setStatus(PostStatus.PUBLIC);
        }

        post.setLastUpdate(LocalDate.now());
        return Optional.of(post);
    }
}
