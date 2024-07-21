package com.fiiiiive.zippop.post.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fiiiiive.zippop.comment.model.Comment;
import com.fiiiiive.zippop.member.model.Customer;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;
    private String postTitle;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_idx")
    @JsonBackReference
    private Customer customer;
    private String email;
    private String postContent;
    private String postDate;
    @OneToMany(mappedBy = "post")
    private List<Comment> comments;
}
