package com.fiiiiive.zippop.post;

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

    public void register(CreatePostReq createPostReq) {
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
            throw new RuntimeException("Customer not found");
        }
    }

    public List<GetPostRes> findByCustomerEmail(String email) {
        Long start = System.currentTimeMillis();
        Optional<List<Post>> result = postRepository.findByEmail(email);
        Long end = System.currentTimeMillis();
        Long diff = end - start;
        if (result.isPresent()) {
            List<GetPostRes> getPostResList = new ArrayList<>();
            for (Post post : result.get()) {
                GetPostRes getPostRes = GetPostRes.builder()
                        .postTitle(post.getPostTitle())
                        .postContent(post.getPostContent())
                        .email(post.getCustomer().getEmail())
                        .postDate(post.getPostDate())
                        .build();
                getPostResList.add(getPostRes);
            }
            System.out.println("##########################{걸린 시간 : " + diff + " }##############################");
            System.out.println("성능 개선 전 끝");
            start = System.currentTimeMillis();
            result = postRepository.findByEmailFetchJoin(email);
            end = System.currentTimeMillis();
            diff = end - start;
            System.out.println("##########################{걸린 시간 : " + diff + " }##############################");
            System.out.println("성능 개선 후 끝");
            return getPostResList;
        } else{
            throw new RuntimeException("Customer not found");
        }
    }

    public Page<Post> getAllPostsPaged(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return postRepository.findPageBy(pageable);
    }
}
