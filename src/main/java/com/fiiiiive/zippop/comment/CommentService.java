package com.fiiiiive.zippop.comment;

import com.fiiiiive.zippop.comment.model.Comment;
import com.fiiiiive.zippop.comment.model.request.CreateCommentReq;
import com.fiiiiive.zippop.comment.model.request.CreateCommentRes;
import com.fiiiiive.zippop.comment.model.response.GetCommentRes;
import com.fiiiiive.zippop.common.exception.BaseException;
import com.fiiiiive.zippop.common.responses.BaseResponseMessage;
import com.fiiiiive.zippop.member.CustomerRepository;
import com.fiiiiive.zippop.member.model.CustomUserDetails;
import com.fiiiiive.zippop.member.model.Customer;
import com.fiiiiive.zippop.post.PostRepository;
import com.fiiiiive.zippop.post.model.Post;
import com.fiiiiive.zippop.post.model.PostImage;
import com.fiiiiive.zippop.post.model.response.GetPostImageRes;
import com.fiiiiive.zippop.post.model.response.GetPostRes;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final CustomerRepository customerRepository;

    public CreateCommentRes register(CustomUserDetails customUserDetails, Long postIdx, CreateCommentReq dto) throws BaseException {
        Post post = postRepository.findById(postIdx)
                .orElseThrow(() ->  new BaseException(BaseResponseMessage.COMMENT_CREATE_FAIL_MEMBER_NOT_FOUND));
        Customer customer = customerRepository.findById(customUserDetails.getIdx())
                .orElseThrow(() -> new BaseException(BaseResponseMessage.COMMENT_CREATE_FAIL_POST_NOT_FOUND));
        Comment comment = Comment.builder()
                .post(post)
                .customer(customer)
                .commentContent(dto.getCommentContent())
                .build();
        commentRepository.save(comment);
        return CreateCommentRes.builder()
                .commentIdx(comment.getCommentIdx())
                .customerEmail(customUserDetails.getEmail())
                .commentContent(comment.getCommentContent())
                .build();

    }

    public Page<GetCommentRes> searchByCustomer(int page, int size, CustomUserDetails customUserDetails) throws BaseException {
        Pageable pageable = PageRequest.of(page, size);
        Page<Comment> result = commentRepository.findByCustomerEmail(customUserDetails.getEmail(), pageable);
        if (result.hasContent()) {
            Page<GetCommentRes> getCommentResPage = result.map(comment-> {
                return GetCommentRes.builder()
                        .commentIdx(comment.getCommentIdx())
                        .customerEmail(comment.getCustomer().getEmail())
                        .commentContent(comment.getCommentContent())
                        .createdAt(comment.getCreatedAt())
                        .createdAt(comment.getCreatedAt())
                        .updatedAt(comment.getUpdatedAt())
                        .build();
            });
            return getCommentResPage;
        } else {
            throw new BaseException(BaseResponseMessage.COMMENT_NOT_FOUND);
        }
    }

    public Page<GetCommentRes> searchByPost(int page, int size, Long postIdx) throws BaseException {
        Pageable pageable = PageRequest.of(page, size);
        Page<Comment> result = commentRepository.findByPostIdx(postIdx, pageable);
        if(result.hasContent()){
            Page<GetCommentRes> getCommentResPage = result.map(comment-> {
                return GetCommentRes.builder()
                        .commentIdx(comment.getCommentIdx())
                        .customerEmail(comment.getCustomer().getEmail())
                        .commentContent(comment.getCommentContent())
                        .createdAt(comment.getCreatedAt())
                        .createdAt(comment.getCreatedAt())
                        .updatedAt(comment.getUpdatedAt())
                        .build();
            });
            return getCommentResPage;
        }else {
            throw new BaseException(BaseResponseMessage.COMMENT_NOT_FOUND);
        }
    }
}
