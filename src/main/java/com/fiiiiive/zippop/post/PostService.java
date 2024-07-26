package com.fiiiiive.zippop.post;

import com.fiiiiive.zippop.common.exception.BaseException;
import com.fiiiiive.zippop.common.responses.BaseResponseMessage;
import com.fiiiiive.zippop.member.CustomerRepository;
import com.fiiiiive.zippop.member.model.CustomUserDetails;
import com.fiiiiive.zippop.member.model.Customer;
import com.fiiiiive.zippop.post.model.Post;
import com.fiiiiive.zippop.post.model.response.CreatePostRes;
import com.fiiiiive.zippop.post.model.response.GetPostRes;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final CustomerRepository customerRepository;

    public CreatePostRes register(CustomUserDetails customUserDetails, CreatePostRes createPostReq) throws BaseException {
        Optional<Customer> result = customerRepository.findByEmail(customUserDetails.getEmail());
        if (result.isPresent()) {
            Customer customer = result.get();
            Post post = Post.builder()
                    .postTitle(createPostReq.getPostTitle())
                    .postContent(createPostReq.getPostContent())
                    .customerEmail(customUserDetails.getEmail())
                    .customer(customer)
                    .build();
            postRepository.save(post);
            return CreatePostRes.builder()
                    .postIdx(post.getPostIdx())
                    .customerEmail(post.getCustomerEmail())
                    .postTitle(post.getPostTitle())
                    .postContent(post.getPostContent())
                    .createdAt(post.getCreatedAt())
                    .build();
        } else {
            throw new BaseException(BaseResponseMessage.POST_REGISTER_FAIL_NOT_FOUND_MEMBER);
        }
    }

    public Page<GetPostRes> searchByCustomer(String email, int page, int size) throws BaseException {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> result = postRepository.findByCustomerEmail(email, pageable);
        if (result.hasContent()) {
            Page<GetPostRes> getPostResPage = result.map(post -> GetPostRes.builder()
                    .postIdx(post.getPostIdx())
                    .postTitle(post.getPostTitle())
                    .postContent(post.getPostContent())
                    .customerEmail(post.getCustomer().getEmail())
                    .createdAt(post.getCreatedAt())
                    .build());
            return getPostResPage;
        } else {
            throw new BaseException(BaseResponseMessage.POST_SEARCH_BY_EMAIL_FAIL);
        }
    }

    public Page<GetPostRes> searchAll(int page, int size) throws BaseException {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> result = postRepository.findAll(pageable);
        if (result.hasContent()) {
            Page<GetPostRes> getPostResPage = result.map(post -> GetPostRes.builder()
                    .postIdx(post.getPostIdx())
                    .postTitle(post.getPostTitle())
                    .postContent(post.getPostContent())
                    .customerEmail(post.getCustomer().getEmail())
                    .createdAt(post.getCreatedAt())
                    .build());
            return getPostResPage;
        } else {
            throw new BaseException(BaseResponseMessage.POST_SEARCH_ALL_FAIL);
        }
    }
}
