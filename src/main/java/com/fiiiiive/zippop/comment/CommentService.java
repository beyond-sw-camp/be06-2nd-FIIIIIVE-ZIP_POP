package com.fiiiiive.zippop.comment;

import com.fiiiiive.zippop.comment.model.Comment;
import com.fiiiiive.zippop.comment.model.request.CreateCommentReq;
import com.fiiiiive.zippop.comment.model.response.GetCommentRes;
import com.fiiiiive.zippop.common.exception.BaseException;
import com.fiiiiive.zippop.common.responses.BaseResponseMessage;
import com.fiiiiive.zippop.member.CustomerRepository;
import com.fiiiiive.zippop.member.model.Customer;
import com.fiiiiive.zippop.post.PostRepository;
import com.fiiiiive.zippop.post.model.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final CustomerRepository customerRepository;

    public void createComment(Long postId, CreateCommentReq createCommentReq) throws BaseException {
        Optional<Post> post = postRepository.findById(postId);
        Optional<Customer> customer = customerRepository.findByEmail(createCommentReq.getEmail());

        if (post.isEmpty()) {
            throw new BaseException(BaseResponseMessage.POST_SEARCH_FAIL);
        }

        if (customer.isEmpty()) {
            throw new BaseException(BaseResponseMessage.MEMBER_LOGIN_FAIL_NOT_FOUND);
        }

        Comment comment = Comment.builder()
                .post(post.get())
                .customer(customer.get())
                .content(createCommentReq.getContent())
                .createdDate(LocalDateTime.now())
                .build();

        try {
            commentRepository.save(comment);
        } catch (Exception e) {
            throw new BaseException(BaseResponseMessage.COMMENT_CREATE_FAIL);
        }
    }

    public Page<GetCommentRes> getCommentsByPost(Long postId, Pageable pageable) throws BaseException {
        Page<Comment> comments = commentRepository.findByPost_Idx(postId, pageable);
        if (comments.isEmpty()) {
            throw new BaseException(BaseResponseMessage.COMMENT_NOT_FOUND);
        }

        return comments.map(comment -> GetCommentRes.builder()
                .commentId(comment.getCommentId())
                .content(comment.getContent())
                .email(comment.getCustomer().getEmail())
                .createdDate(comment.getCreatedDate().toString())
                .build());
    }

    public Page<GetCommentRes> getCommentsByCustomerEmail(String email, Pageable pageable) throws BaseException {
        Page<Comment> comments = commentRepository.findByCustomerEmail(email, pageable);
        if (comments.isEmpty()) {
            throw new BaseException(BaseResponseMessage.COMMENT_NOT_FOUND);
        }

        return comments.map(comment -> GetCommentRes.builder()
                .commentId(comment.getCommentId())
                .content(comment.getContent())
                .email(comment.getCustomer().getEmail())
                .createdDate(comment.getCreatedDate().toString())
                .build());
    }


}
