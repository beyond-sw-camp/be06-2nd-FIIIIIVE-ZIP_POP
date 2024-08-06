package com.fiiiiive.zippop.popup_store.model;

import com.fiiiiive.zippop.member.model.Customer;
import com.fiiiiive.zippop.post.model.Post;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class PopupStoreLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long storeLikeIdx;

    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="postIdx")
    private PopupStore popupStore;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="customerIdx")
    private Customer customer;
}

