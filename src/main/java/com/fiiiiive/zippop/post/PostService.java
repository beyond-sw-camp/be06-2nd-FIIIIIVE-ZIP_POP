package com.fiiiiive.zippop.post;

import com.fiiiiive.zippop.common.exception.BaseException;
import com.fiiiiive.zippop.common.responses.BaseResponseMessage;
import com.fiiiiive.zippop.post.model.Post;
import com.fiiiiive.zippop.post.model.request.CreatePostReq;
import com.fiiiiive.zippop.post.model.response.GetPostRes;
import com.fiiiiive.zippop.post.PostRepository;
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

    public void register(CreatePostReq createPostReq) throws BaseException {
        // Post 엔티티 생성 및 저장
        Post post = Post.builder()
                .postTitle(createPostReq.getPostTitle())
                .postContent(createPostReq.getPostContent())
                .postDate(createPostReq.getPostDate())
                .email(createPostReq.getEmail())
                .build();

        try {
            postRepository.save(post);
        } catch (Exception e) {
            throw new BaseException(BaseResponseMessage.POST_REGISTER_FAIL);
        }
    }

    public List<GetPostRes> findByCustomerEmail(String email) throws BaseException {
        Optional<List<Post>> result = postRepository.findByEmail(email);

        if (result.isEmpty()) {
            throw new BaseException(BaseResponseMessage.POST_SEARCH_FAIL);
        }

        List<GetPostRes> getPostResList = new ArrayList<>();
        for (Post post : result.get()) {
            GetPostRes getPostRes = GetPostRes.builder()
                    .postTitle(post.getPostTitle())
                    .postContent(post.getPostContent())
                    .email(post.getEmail())
                    .postDate(post.getPostDate())
                    .build();
            getPostResList.add(getPostRes);
        }

        return getPostResList;
    }

    public Page<Post> getAllPostsPaged(int page, int size) throws BaseException {
        try {
            Pageable pageable = PageRequest.of(page, size);
            return postRepository.findPageBy(pageable);
        } catch (Exception e) {
            throw new BaseException(BaseResponseMessage.POST_SEARCH_FAIL);
        }
    }
}
