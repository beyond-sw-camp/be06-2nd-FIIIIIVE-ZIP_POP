package com.fiiiiive.zippop.post;

import com.fiiiiive.zippop.member.CustomerRepository;
import com.fiiiiive.zippop.member.model.Customer;
import com.fiiiiive.zippop.post.model.Post;
import com.fiiiiive.zippop.post.model.request.PostReq;
import com.fiiiiive.zippop.post.model.response.PostRes;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final CustomerRepository customerRepository;
    public PostService(PostRepository postRepository, CustomerRepository customerRepository) {
        this.postRepository = postRepository;
        this.customerRepository = customerRepository;
    }

    public void register(PostReq postReq)
    {
        Post post = Post.builder()
                .postTitle(postReq.getPostTitle())
                .postContent(postReq.getPostContent())
                .postDate(postReq.getPostDate())
                .build();
        Optional<Customer> customer = customerRepository.findByEmail(postReq.getEmail());
        if(customer.isPresent())
        {
            post.setCustomer(customer.get());
            post.setEmail(customer.get().getEmail());
            postRepository.save(post);
            customer.get().getPostsList().add(post);
        } else {
            throw new RuntimeException("Customer not found");
        }
    }

    public Optional<List<PostRes>> findByCustomerEmail(String email){
        List<Post> postList = postRepository.findByEmail(email);
        if(postList.isEmpty()){
            return Optional.empty();
        }
        List<PostRes> postResList = new ArrayList<>();
        for(Post post : postList){
            PostRes postRes = new PostRes();
            postRes = postRes.convertToPostRes(post);
            postRes.setEmail(post.getCustomer().getEmail());
            postResList.add(postRes);
        }
        return Optional.of(postResList);
    }
}
