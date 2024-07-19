package com.fiiiiive.zippop.member.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fiiiiive.zippop.post.model.Post;
import com.fiiiiive.zippop.post.model.response.GetPostRes;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;
    private String email;
    private String password;
    private String name;
    private String phoneNumber;
    private String address1;
    private String address2;
    private Integer point;
    private String role;
    private Boolean enabled;

    @BatchSize(size=10)
    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Post> postsList = new ArrayList<>();
}
