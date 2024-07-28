package com.fiiiiive.zippop.comment;

import com.fiiiiive.zippop.comment.model.Comment;
import com.fiiiiive.zippop.comment.model.CommentLike;
import com.fiiiiive.zippop.comment.model.request.CreateCommentReq;
import com.fiiiiive.zippop.comment.model.request.UpdateCommentReq;
import com.fiiiiive.zippop.comment.model.response.CreateCommentRes;
import com.fiiiiive.zippop.comment.model.response.GetCommentRes;
import com.fiiiiive.zippop.comment.model.response.UpdateCommentRes;
import com.fiiiiive.zippop.common.exception.BaseException;
import com.fiiiiive.zippop.common.responses.BaseResponseMessage;
import com.fiiiiive.zippop.member.CustomerRepository;
import com.fiiiiive.zippop.member.model.CustomUserDetails;
import com.fiiiiive.zippop.member.model.Customer;
import com.fiiiiive.zippop.post.PostRepository;
import com.fiiiiive.zippop.post.model.Post;
import com.fiiiiive.zippop.post.model.PostLike;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final CustomerRepository customerRepository;
    private final CommentLikeRepository commentLikeRepository;
    public CreateCommentRes register(CustomUserDetails customUserDetails, Long postIdx, CreateCommentReq dto) throws BaseException {
        Post post = postRepository.findById(postIdx)
                .orElseThrow(() ->  new BaseException(BaseResponseMessage.COMMENT_REGISTER_FAIL_INVALID_MEMBER));
        Customer customer = customerRepository.findById(customUserDetails.getIdx())
                .orElseThrow(() -> new BaseException(BaseResponseMessage.COMMENT_REGISTER_FAIL_POST_NOT_FOUND));
        Comment comment = Comment.builder()
                .post(post)
                .customerEmail(customUserDetails.getEmail())
                .commentContent(dto.getCommentContent())
                .commentLikeCount(0)
                .customer(customer)
                .build();
        commentRepository.save(comment);
        return CreateCommentRes.builder()
                .commentIdx(comment.getCommentIdx())
                .customerEmail(customUserDetails.getEmail())
                .commentLikeCount(comment.getCommentLikeCount())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .commentContent(comment.getCommentContent())
                .build();
    }

    public Page<GetCommentRes> searchCustomer(int page, int size, CustomUserDetails customUserDetails) throws BaseException {
        Pageable pageable = PageRequest.of(page, size);
        Page<Comment> result = commentRepository.findByCustomerIdx(customUserDetails.getIdx(), pageable);
        if (result.hasContent()) {
            Page<GetCommentRes> getCommentResPage = result.map(comment->
                    GetCommentRes.builder()
                        .commentIdx(comment.getCommentIdx())
                        .customerEmail(comment.getCustomer().getEmail())
                        .commentContent(comment.getCommentContent())
                        .createdAt(comment.getCreatedAt())
                        .createdAt(comment.getCreatedAt())
                        .updatedAt(comment.getUpdatedAt())
                        .build()
            );
            return getCommentResPage;
        } else {
            throw new BaseException(BaseResponseMessage.COMMENT_SEARCH_BY_CUSTOMER_FAIL);
        }
    }

    public Page<GetCommentRes> searchAll(int page, int size, Long postIdx) throws BaseException {
        Pageable pageable = PageRequest.of(page, size);
        Page<Comment> result = commentRepository.findByPostIdx(postIdx, pageable);
        if(result.hasContent()){
            Page<GetCommentRes> getCommentResPage = result.map(comment->
                GetCommentRes.builder()
                        .commentIdx(comment.getCommentIdx())
                        .customerEmail(comment.getCustomer().getEmail())
                        .commentContent(comment.getCommentContent())
                        .createdAt(comment.getCreatedAt())
                        .updatedAt(comment.getUpdatedAt())
                        .build()
            );
            return getCommentResPage;
        } else {
            throw new BaseException(BaseResponseMessage.COMMENT_SEARCH_ALL_FAIL);
        }
    }

    public UpdateCommentRes update(CustomUserDetails customUserDetails, Long commentIdx, UpdateCommentReq dto) throws BaseException {
        Optional<Comment> result = commentRepository.findById(commentIdx);
        if(result.isPresent()){
            Comment comment = result.get();
            if(Objects.equals(comment.getCustomerEmail(), customUserDetails.getEmail())){
                comment.setCommentContent(dto.getCommentContent());
                commentRepository.save(comment);
                return UpdateCommentRes.builder()
                        .commentIdx(comment.getCommentIdx())
                        .customerEmail(customUserDetails.getEmail())
                        .commentContent(comment.getCommentContent())
                        .createdAt(comment.getCreatedAt())
                        .updatedAt(comment.getUpdatedAt())
                        .build();
            } else {
                throw new BaseException(BaseResponseMessage.COMMENT_UPDATE_FAIL_INVALID_MEMBER);
            }
        } else {
            throw new BaseException(BaseResponseMessage.COMMENT_UPDATE_FAIL_COMMENT_NOT_FOUND);
        }
    }

    public void delete(CustomUserDetails customUserDetails, Long commentIdx) throws BaseException{
        Optional<Comment> result = commentRepository.findById(commentIdx);
        if(result.isPresent()){
            Comment comment = result.get();
            if(Objects.equals(comment.getCustomerEmail(), customUserDetails.getEmail())){
                commentRepository.deleteById(commentIdx);
            } else {
                throw new BaseException(BaseResponseMessage.COMMENT_DELETE_FAIL_INVALID_MEMBER);
            }
        } else {
            throw new BaseException(BaseResponseMessage.COMMENT_DELETE_FAIL_COMMENT_NOT_FOUND);
        }
    }

    @Transactional
    public void like(CustomUserDetails customUserDetails, Long commentIdx) throws BaseException{
        Optional<Comment> resultComment = commentRepository.findById(commentIdx);
        if(resultComment.isPresent()){
            Optional<Customer> resultCustomer = customerRepository.findById(customUserDetails.getIdx());
            if(resultCustomer.isPresent()){
                Comment comment = resultComment.get();
                Customer customer = resultCustomer.get();
                Optional<CommentLike> resultCommentLike = commentLikeRepository.findByCustomerIdxAndCommentIdx(customUserDetails.getIdx(), commentIdx);
                if(resultCommentLike.isEmpty()){
                    comment.setCommentLikeCount(comment.getCommentLikeCount() + 1);
                    commentRepository.save(comment);
                    CommentLike commentLike = CommentLike.builder()
                            .comment(comment)
                            .customer(customer)
                            .build();
                    commentLikeRepository.save(commentLike);
                } else {
                    comment.setCommentLikeCount(comment.getCommentLikeCount() - 1);
                    commentRepository.save(comment);
                    commentLikeRepository.deleteByCustomerIdxAndCommentIdx(customer.getCustomerIdx(), commentIdx);
                }
            }else {
                throw new BaseException(BaseResponseMessage.COMMENT_LIKE_FAIL_INVALID_MEMBER);
            }
        } else {
            throw new BaseException(BaseResponseMessage.COMMENT_LIKE_FAIL_COMMENT_NOT_FOUND);
        }
    }
}
