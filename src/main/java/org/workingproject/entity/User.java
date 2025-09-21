package org.workingproject.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.workingproject.annotation.OurValidation;

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

    @NotBlank
    private String userName;

    @NotBlank
    @Email
    private String email;

//    @NotBlank
//     @Size(min = 3, max = 15)
//    @Pattern(regexp = "^[A-Za-z0-9]+$")
    @OurValidation(message = "Password does not meet security requirements")
    private String password;

    @ManyToOne
    @JoinColumn(name = "role_id")
    public Role role;

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
