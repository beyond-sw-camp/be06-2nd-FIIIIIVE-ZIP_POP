package com.fiiiiive.zippop.post;

import com.fiiiiive.zippop.comment.model.Comment;
import com.fiiiiive.zippop.comment.model.response.GetCommentRes;
import com.fiiiiive.zippop.common.exception.BaseException;
import com.fiiiiive.zippop.common.responses.BaseResponseMessage;
import com.fiiiiive.zippop.member.CustomerRepository;
import com.fiiiiive.zippop.member.model.CustomUserDetails;
import com.fiiiiive.zippop.member.model.Customer;
import com.fiiiiive.zippop.popup_store.model.PopupStoreImage;
import com.fiiiiive.zippop.popup_store.model.response.GetPopupStoreImageRes;
import com.fiiiiive.zippop.post.model.Post;
import com.fiiiiive.zippop.post.model.PostImage;
import com.fiiiiive.zippop.post.model.PostLike;
import com.fiiiiive.zippop.post.model.request.UpdatePostReq;
import com.fiiiiive.zippop.post.model.response.CreatePostRes;
import com.fiiiiive.zippop.post.model.response.GetPostImageRes;
import com.fiiiiive.zippop.post.model.response.GetPostRes;
import com.fiiiiive.zippop.post.model.response.UpdatePostRes;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final CustomerRepository customerRepository;
    private final PostImageRepository postImageRepository;
    private final PostLikeRepository postLikeRepository;

    public CreatePostRes register(CustomUserDetails customUserDetails, List<String> fileNames,  CreatePostRes dto) throws BaseException {
        Customer customer = customerRepository.findByCustomerEmail(customUserDetails.getEmail())
        .orElseThrow(() -> new BaseException(BaseResponseMessage.POST_REGISTER_FAIL_INVALID_MEMBER));
        Post post = Post.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .likeCount(0)
                .customerEmail(customUserDetails.getEmail())
                .customer(customer)
                .build();
        postRepository.save(post);
        List<GetPostImageRes> getPostImageResList = new ArrayList<>();
        for(String fileName : fileNames){
            PostImage postImage = PostImage.builder()
                    .imageUrl(fileName)
                    .post(post)
                    .build();
            postImageRepository.save(postImage);
            GetPostImageRes getPostImageRes = GetPostImageRes.builder()
                    .postImageIdx(postImage.getPostImageIdx())
                    .imageUrl(postImage.getImageUrl())
                    .createdAt(postImage.getCreatedAt())
                    .updatedAt(postImage.getUpdatedAt())
                    .build();
            getPostImageResList.add(getPostImageRes);
        }
        return CreatePostRes.builder()
                .postIdx(post.getPostIdx())
                .customerEmail(post.getCustomerEmail())
                .title(post.getTitle())
                .content(post.getContent())
                .likeCount(post.getLikeCount())
                .getPostImageResList(getPostImageResList)
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }

    public GetPostRes search(Long postIdx) throws BaseException {
        Post post = postRepository.findByPostIdx(postIdx)
                .orElseThrow(() -> new BaseException(BaseResponseMessage.POST_SEARCH_BY_IDX_FAIL));
        List<PostImage> postImageList = post.getPostImageList();
        List<GetPostImageRes> getPostImageResList = new ArrayList<>();
        for(PostImage postImage: postImageList) {
            GetPostImageRes getPostImageRes = GetPostImageRes.builder()
                    .postImageIdx(postImage.getPostImageIdx())
                    .imageUrl(postImage.getImageUrl())
                    .createdAt(postImage.getCreatedAt())
                    .updatedAt(postImage.getUpdatedAt())
                    .build();
            getPostImageResList.add(getPostImageRes);
        }
        return GetPostRes.builder()
                .postIdx(post.getPostIdx())
                .customerEmail(post.getCustomerEmail())
                .title(post.getTitle())
                .content(post.getContent())
                .likeCount(post.getLikeCount())
                .commentCount(post.getCommentList().size())
                .getPostImageResList(getPostImageResList)
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }

    public Page<GetPostRes> searchAll(int page, int size) throws BaseException {
        Page<Post> result = postRepository.findAll(PageRequest.of(page, size));
        if (!result.hasContent()) {
            throw new BaseException(BaseResponseMessage.POST_SEARCH_ALL_FAIL);
        }
        Page<GetPostRes> getPostResPage = result.map(post-> {
        List<PostImage> postImages = post.getPostImageList();
        List<GetPostImageRes> getPostImageResList = new ArrayList<>();
        for (PostImage postImage : postImages) {
            GetPostImageRes getPostImageRes = GetPostImageRes.builder()
                    .postImageIdx(postImage.getPostImageIdx())
                    .imageUrl(postImage.getImageUrl())
                    .createdAt(post.getCreatedAt())
                    .updatedAt(post.getUpdatedAt())
                    .build();
            getPostImageResList.add(getPostImageRes);
        }
        return GetPostRes.builder()
                .postIdx(post.getPostIdx())
                .customerEmail(post.getCustomer().getEmail())
                .title(post.getTitle())
                .content(post.getContent())
                .likeCount(post.getLikeCount())
                .commentCount(post.getCommentList().size())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .getPostImageResList(getPostImageResList)
                .build();
        });
        return getPostResPage;
    }

    public Page<GetPostRes> searchCustomer(CustomUserDetails customUserDetails, int page, int size) throws BaseException {
        Page<Post> result = postRepository.findByCustomerEmail(customUserDetails.getEmail(), PageRequest.of(page, size))
                .orElseThrow(() -> new BaseException(BaseResponseMessage.POST_SEARCH_BY_CUSTOMER_FAIL));
        Page<GetPostRes> getPostResPage = result.map(post-> {
            List<PostImage> postImages = post.getPostImageList();
            List<GetPostImageRes> getPostImageResList = new ArrayList<>();
            for (PostImage postImage : postImages) {
                GetPostImageRes getPostImageRes = GetPostImageRes.builder()
                        .postImageIdx(postImage.getPostImageIdx())
                        .imageUrl(postImage.getImageUrl())
                        .createdAt(post.getCreatedAt())
                        .updatedAt(post.getUpdatedAt())
                        .build();
                getPostImageResList.add(getPostImageRes);
            }
            return GetPostRes.builder()
                    .postIdx(post.getPostIdx())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .likeCount(post.getLikeCount())
                    .getPostImageResList(getPostImageResList)
                    .customerEmail(post.getCustomer().getEmail())
                    .createdAt(post.getCreatedAt())
                    .updatedAt(post.getUpdatedAt())
                    .build();
        });
        return getPostResPage;
    }

    public Page<GetPostRes> searchKeyword(String keyword, int page, int size) throws BaseException {
        Page<Post> result = postRepository.findByKeyword(keyword, PageRequest.of(page, size))
        .orElseThrow(() -> new BaseException(BaseResponseMessage.POST_SEARCH_BY_IDX_FAIL));
        Page<GetPostRes> getPostResPage = result.map(post-> {
            List<PostImage> postImages = post.getPostImageList();
            List<GetPostImageRes> getPostImageResList = new ArrayList<>();
            for (PostImage postImage : postImages) {
                GetPostImageRes getPostImageRes = GetPostImageRes.builder()
                        .postImageIdx(postImage.getPostImageIdx())
                        .imageUrl(postImage.getImageUrl())
                        .createdAt(post.getCreatedAt())
                        .updatedAt(post.getUpdatedAt())
                        .build();
                getPostImageResList.add(getPostImageRes);
            }
            return GetPostRes.builder()
                    .postIdx(post.getPostIdx())
                    .customerEmail(post.getCustomer().getEmail())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .getPostImageResList(getPostImageResList)
                    .createdAt(post.getCreatedAt())
                    .updatedAt(post.getUpdatedAt())
                    .build();
        });
        return getPostResPage;
    }

    public UpdatePostRes update(CustomUserDetails customUserDetails, Long postIdx, UpdatePostReq dto, List<String> fileNames) throws BaseException {
        Post post = postRepository.findByPostIdx(postIdx)
        .orElseThrow(() -> new BaseException(BaseResponseMessage.POST_UPDATE_FAIL_NOT_FOUND_POST));
        if(!Objects.equals(post.getCustomerEmail(), customUserDetails.getEmail())) {
            throw new BaseException(BaseResponseMessage.POST_UPDATE_FAIL_INVALID_MEMBER);
        }
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        postRepository.save(post);
        List<PostImage> postImageList = postImageRepository.findByPostIdx(post.getPostIdx()).orElse(new ArrayList<>());
        List<GetPostImageRes> getPostImageResList = new ArrayList<>();
        for(PostImage postImage : postImageList){
            postImageRepository.deleteById(postImage.getPostImageIdx());
        }
        for (String fileName: fileNames) {
            PostImage postImage = PostImage.builder()
                    .imageUrl(fileName)
                    .post(post)
                    .build();
            postImageRepository.save(postImage);
            GetPostImageRes getPostImageRes = GetPostImageRes.builder()
                    .postImageIdx(postImage.getPostImageIdx())
                    .imageUrl(postImage.getImageUrl())
                    .createdAt(postImage.getCreatedAt())
                    .updatedAt(postImage.getUpdatedAt())
                    .build();
            getPostImageResList.add(getPostImageRes);
        }
        return UpdatePostRes.builder()
                .postIdx(post.getPostIdx())
                .customerEmail(post.getCustomerEmail())
                .title(post.getTitle())
                .content(post.getContent())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .getPostImageResList(getPostImageResList)
                .build();
    }

    public void delete(CustomUserDetails customUserDetails, Long postIdx) throws BaseException{
        Post post = postRepository.findByPostIdx(postIdx)
        .orElseThrow(() -> new BaseException(BaseResponseMessage.POST_DELETE_FAIL_POST_NOT_FOUND));
        if(!Objects.equals(post.getCustomerEmail(), customUserDetails.getEmail())){
            throw new BaseException(BaseResponseMessage.POST_DELETE_FAIL_INVALID_MEMBER);
        }
        postRepository.deleteById(postIdx);
    }

    @Transactional
    public void like(CustomUserDetails customUserDetails, Long postIdx) throws BaseException{
        Post post = postRepository.findByPostIdx(postIdx)
        .orElseThrow(() -> new BaseException(BaseResponseMessage.POST_LIKE_FAIL_POST_NOT_FOUND));
        Customer customer = customerRepository.findById(customUserDetails.getIdx())
        .orElseThrow(() -> new BaseException(BaseResponseMessage.POST_LIKE_FAIL_INVALID_MEMBER));
        Optional<PostLike> result = postLikeRepository.findByCustomerEmailAndPostIdx(customUserDetails.getEmail(), postIdx);
        if (result.isEmpty()) {
            post.setLikeCount(post.getLikeCount() + 1);
            postRepository.save(post);
            PostLike postLike = PostLike.builder()
                    .post(post)
                    .customer(customer)
                    .build();
            postLikeRepository.save(postLike);
        } else {
            post.setLikeCount(post.getLikeCount() - 1);
            postRepository.save(post);
            postLikeRepository.deleteByCustomerEmailAndPostIdx(customer.getEmail(), postIdx);
        }
    }
}
