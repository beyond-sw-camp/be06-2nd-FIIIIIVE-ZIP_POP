package com.fiiiiive.zippop.member.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fiiiiive.zippop.cart.model.Cart;
import com.fiiiiive.zippop.comment.model.Comment;
import com.fiiiiive.zippop.favorite.model.Favorite;
import com.fiiiiive.zippop.orders.model.CustomerOrders;
import com.fiiiiive.zippop.popup_review.model.PopupReview;
import com.fiiiiive.zippop.post.model.Post;
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
    private Long customerIdx;
    @Column(nullable = false, length = 100, unique = true)
    private String email;
    private String password;
    private String name;
    private String phoneNumber;
    private String address;
    private Integer point;
    private String role;
    private Boolean enabled;
    private Boolean inactive;

    @BatchSize(size=10)
    @OneToMany(mappedBy = "customer")
    @JsonManagedReference
    private List<Post> postList = new ArrayList<>();
    @OneToMany(mappedBy = "customer")
    private List<Cart> cartList = new ArrayList<>();
    @OneToMany(mappedBy = "customer")
    private List<Favorite> favoriteList;
    @BatchSize(size=10)
    @OneToMany(mappedBy = "customer")
    private List<Comment> commentList;
    @OneToMany(mappedBy = "customer")
    private List<PopupReview> popupReviewList;
    @OneToMany(mappedBy = "customer")
    private List<CustomerOrders> customerOrdersList;
}
