package com.fiiiiive.zippop.post;

import com.fiiiiive.zippop.common.exception.BaseException;
import com.fiiiiive.zippop.common.responses.BaseResponseMessage;
import com.fiiiiive.zippop.member.CustomerRepository;
import com.fiiiiive.zippop.member.model.Customer;
import com.fiiiiive.zippop.post.model.Post;
import com.fiiiiive.zippop.post.model.request.CreatePostReq;
import com.fiiiiive.zippop.post.model.response.GetPostRes;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CustomerRepository customerRepository;

    public void register(CreatePostReq createPostReq) throws BaseException {
        Post post = Post.builder()
                .postTitle(createPostReq.getPostTitle())
                .postContent(createPostReq.getPostContent())
                .postDate(createPostReq.getPostDate())
                .build();
        Optional<Customer> customer = customerRepository.findByEmail(createPostReq.getEmail());
        if (customer.isPresent()) {
            post.setCustomer(customer.get());
            post.setEmail(customer.get().getEmail());
            postRepository.save(post);
            customer.get().getPostsList().add(post);
        } else {
            throw new BaseException(BaseResponseMessage.POST_REGISTER_FAIL);
        }
    }

    public Page<GetPostRes> findByCustomerEmail(String email, Pageable pageable) throws BaseException {
        Long start = System.currentTimeMillis();
        Page<Post> result = postRepository.findByEmail(email, pageable);
        Long end = System.currentTimeMillis();
        Long diff = end - start;

        if (result.hasContent()) {
            Page<GetPostRes> getPostResPage = result.map(post -> GetPostRes.builder()
                    .postTitle(post.getPostTitle())
                    .postContent(post.getPostContent())
                    .email(post.getCustomer().getEmail())
                    .postDate(post.getPostDate())
                    .build());

            System.out.println("##########################{걸린 시간 : " + diff + " }##############################");
            System.out.println("성능 개선 전 끝");

            start = System.currentTimeMillis();
            Page<Post> fetchJoinResult = postRepository.findByEmailFetchJoin(email, pageable);
            end = System.currentTimeMillis();
            diff = end - start;

            System.out.println("##########################{걸린 시간 : " + diff + " }##############################");
            System.out.println("성능 개선 후 끝");

            return getPostResPage;
        } else {
            throw new BaseException(BaseResponseMessage.POST_SEARCH_FAIL);
        }
    }

    public Page<Post> getAllPostsPaged(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return postRepository.findPageBy(pageable);
    }
}
