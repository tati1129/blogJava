package org.workingproject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String userName;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
    @OneToMany(mappedBy = "user", cascade =CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();
    private LocalDate createDate;
    private LocalDate lastUpdate;

    // хелпер для двусторонней связи
    public void addPost(Post post){
        post.setUser(this);
        posts.add(post);
    }
    public void removeTask(Post post){
        post.setUser(null);
        posts.remove(post);
    }
}
