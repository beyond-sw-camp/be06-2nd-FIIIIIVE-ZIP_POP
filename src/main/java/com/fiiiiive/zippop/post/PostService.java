package com.fiiiiive.zippop.post;

import com.fiiiiive.zippop.comment.model.Comment;
import com.fiiiiive.zippop.comment.model.response.GetCommentRes;
import com.fiiiiive.zippop.common.exception.BaseException;
import com.fiiiiive.zippop.common.responses.BaseResponseMessage;
import com.fiiiiive.zippop.member.CustomerRepository;
import com.fiiiiive.zippop.member.model.CustomUserDetails;
import com.fiiiiive.zippop.member.model.Customer;
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
        Optional<Customer> result = customerRepository.findById(customUserDetails.getIdx());
        if (result.isPresent()) {
            Customer customer = result.get();
            Post post = Post.builder()
                    .postTitle(dto.getPostTitle())
                    .postContent(dto.getPostContent())
                    .postLikeCount(0)
                    .customerEmail(customUserDetails.getEmail())
                    .customer(customer)
                    .build();
            postRepository.save(post);
            List<GetPostImageRes> getPostImageResList = new ArrayList<>();
            for(String fileName : fileNames){
                PostImage postImage = PostImage.builder()
                        .postImageUrl(fileName)
                        .post(post)
                        .build();
                postImageRepository.save(postImage);
                GetPostImageRes getPostImageRes = GetPostImageRes.builder()
                        .postImageIdx(postImage.getPostImageIdx())
                        .postImageUrl(postImage.getPostImageUrl())
                        .createdAt(postImage.getCreatedAt())
                        .updatedAt(postImage.getUpdatedAt())
                        .build();
                getPostImageResList.add(getPostImageRes);
            }
            return CreatePostRes.builder()
                    .postIdx(post.getPostIdx())
                    .customerEmail(post.getCustomerEmail())
                    .postTitle(post.getPostTitle())
                    .postContent(post.getPostContent())
                    .postLikeCount(post.getPostLikeCount())
                    .postImages(getPostImageResList)
                    .createdAt(post.getCreatedAt())
                    .updatedAt(post.getUpdatedAt())
                    .build();
        } else {
            throw new BaseException(BaseResponseMessage.POST_REGISTER_FAIL_INVALID_MEMBER);
        }
    }

    public Page<GetPostRes> searchCustomer(CustomUserDetails customUserDetails, int page, int size) throws BaseException {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> result = postRepository.findByCustomerEmail(customUserDetails.getEmail(), pageable);
        if (result.hasContent()) {
            Page<GetPostRes> getPostResPage = result.map(post-> {
                List<PostImage> postImages = post.getPostImages();
                List<GetPostImageRes> getPostImageResList = new ArrayList<>();
                for (PostImage postImage : postImages) {
                    GetPostImageRes getPostImageRes = GetPostImageRes.builder()
                            .postImageIdx(postImage.getPostImageIdx())
                            .postImageUrl(postImage.getPostImageUrl())
                            .createdAt(post.getCreatedAt())
                            .updatedAt(post.getUpdatedAt())
                            .build();
                    getPostImageResList.add(getPostImageRes);
                }
                List<Comment> commentList = post.getComments();
                List<GetCommentRes> getCommentResList = new ArrayList<>();
                for(Comment comment: commentList){
                    GetCommentRes getCommentRes = GetCommentRes.builder()
                            .commentIdx(comment.getCommentIdx())
                            .commentContent(comment.getCommentContent())
                            .customerEmail(comment.getCustomer().getEmail())
                            .createdAt(comment.getCreatedAt())
                            .updatedAt(comment.getUpdatedAt())
                            .build();
                    getCommentResList.add(getCommentRes);
                }
                return GetPostRes.builder()
                        .postIdx(post.getPostIdx())
                        .postTitle(post.getPostTitle())
                        .postContent(post.getPostContent())
                        .postLikeCount(post.getPostLikeCount())
                        .getPostImageRes(getPostImageResList)
                        .getCommentRes(getCommentResList)
                        .customerEmail(post.getCustomer().getEmail())
                        .createdAt(post.getCreatedAt())
                        .updatedAt(post.getUpdatedAt())
                        .build();
            });
            return getPostResPage;
        } else {
            throw new BaseException(BaseResponseMessage.POST_SEARCH_BY_CUSTOMER_FAIL);
        }
    }

    public Page<GetPostRes> searchAll(int page, int size) throws BaseException {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> result = postRepository.findAll(pageable);
        if (result.hasContent()) {
            Page<GetPostRes> getPostResPage = result.map(post-> {
                List<PostImage> postImages = post.getPostImages();
                List<GetPostImageRes> getPostImageResList = new ArrayList<>();
                for (PostImage postImage : postImages) {
                    GetPostImageRes getPostImageRes = GetPostImageRes.builder()
                            .postImageIdx(postImage.getPostImageIdx())
                            .postImageUrl(postImage.getPostImageUrl())
                            .createdAt(post.getCreatedAt())
                            .updatedAt(post.getUpdatedAt())
                            .build();
                    getPostImageResList.add(getPostImageRes);
                }
                List<Comment> commentList = post.getComments();
                List<GetCommentRes> getCommentResList = new ArrayList<>();
                for(Comment comment: commentList){
                    GetCommentRes getCommentRes = GetCommentRes.builder()
                            .commentIdx(comment.getCommentIdx())
                            .commentContent(comment.getCommentContent())
                            .customerEmail(comment.getCustomer().getEmail())
                            .createdAt(comment.getCreatedAt())
                            .updatedAt(comment.getUpdatedAt())
                            .build();
                    getCommentResList.add(getCommentRes);
                }
                return GetPostRes.builder()
                        .postIdx(post.getPostIdx())
                        .postTitle(post.getPostTitle())
                        .postContent(post.getPostContent())
                        .postLikeCount(post.getPostLikeCount())
                        .getPostImageRes(getPostImageResList)
                        .getCommentRes(getCommentResList)
                        .customerEmail(post.getCustomer().getEmail())
                        .createdAt(post.getCreatedAt())
                        .updatedAt(post.getUpdatedAt())
                        .build();
            });
            return getPostResPage;
        } else {
            throw new BaseException(BaseResponseMessage.POST_SEARCH_ALL_FAIL);
        }
    }

    public Page<GetPostRes> searchRecommend(int page, int size, String keyword) throws BaseException {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> result = postRepository.findByKeyword(pageable, keyword);
        if (result.hasContent()) {
            Page<GetPostRes> getPostResPage = result.map(post-> {
                List<PostImage> postImages = post.getPostImages();
                List<GetPostImageRes> getPostImageResList = new ArrayList<>();
                for (PostImage postImage : postImages) {
                    GetPostImageRes getPostImageRes = GetPostImageRes.builder()
                            .postImageIdx(postImage.getPostImageIdx())
                            .postImageUrl(postImage.getPostImageUrl())
                            .createdAt(post.getCreatedAt())
                            .updatedAt(post.getUpdatedAt())
                            .build();
                    getPostImageResList.add(getPostImageRes);
                }
                List<Comment> commentList = post.getComments();
                List<GetCommentRes> getCommentResList = new ArrayList<>();
                for(Comment comment: commentList){
                    GetCommentRes getCommentRes = GetCommentRes.builder()
                            .commentIdx(comment.getCommentIdx())
                            .commentContent(comment.getCommentContent())
                            .customerEmail(comment.getCustomer().getEmail())
                            .createdAt(comment.getCreatedAt())
                            .updatedAt(comment.getUpdatedAt())
                            .build();
                    getCommentResList.add(getCommentRes);
                }
                return GetPostRes.builder()
                        .postIdx(post.getPostIdx())
                        .postTitle(post.getPostTitle())
                        .postContent(post.getPostContent())
                        .getPostImageRes(getPostImageResList)
                        .getCommentRes(getCommentResList)
                        .customerEmail(post.getCustomer().getEmail())
                        .createdAt(post.getCreatedAt())
                        .updatedAt(post.getUpdatedAt())
                        .build();
            });
            return getPostResPage;
        } else {
            throw new BaseException(BaseResponseMessage.POST_SEARCH_BY_IDX_FAIL);
        }
    }

    public GetPostRes search(Long postIdx) throws BaseException {
        Optional<Post> result = postRepository.findById(postIdx);
        if(result.isPresent()){
            Post post = result.get();
            List<PostImage> postImageList = post.getPostImages();
            List<GetPostImageRes> getPostImageResList = new ArrayList<>();
            for(PostImage postImage: postImageList){
                GetPostImageRes getPostImageRes = GetPostImageRes.builder()
                        .postImageIdx(postImage.getPostImageIdx())
                        .postImageUrl(postImage.getPostImageUrl())
                        .createdAt(postImage.getCreatedAt())
                        .updatedAt(postImage.getUpdatedAt())
                        .build();
                getPostImageResList.add(getPostImageRes);
            }
            List<Comment> commentList = post.getComments();
            List<GetCommentRes> getCommentResList = new ArrayList<>();
            for(Comment comment: commentList){
                GetCommentRes getCommentRes = GetCommentRes.builder()
                        .commentIdx(comment.getCommentIdx())
                        .commentContent(comment.getCommentContent())
                        .customerEmail(comment.getCustomer().getEmail())
                        .createdAt(comment.getCreatedAt())
                        .updatedAt(comment.getUpdatedAt())
                        .build();
                getCommentResList.add(getCommentRes);
            }
            return GetPostRes.builder()
                    .postIdx(post.getPostIdx())
                    .customerEmail(post.getCustomerEmail())
                    .postTitle(post.getPostTitle())
                    .postContent(post.getPostContent())
                    .postLikeCount(post.getPostLikeCount())
                    .getPostImageRes(getPostImageResList)
                    .getCommentRes(getCommentResList)
                    .createdAt(post.getCreatedAt())
                    .updatedAt(post.getUpdatedAt())
                    .build();
        } else {
            throw new BaseException(BaseResponseMessage.POST_SEARCH_BY_IDX_FAIL);
        }
    }

    public UpdatePostRes update(CustomUserDetails customUserDetails, Long postIdx, UpdatePostReq dto, List<String> fileNames) throws BaseException {
        Optional<Post> resultPost = postRepository.findById(postIdx);
        if (resultPost.isPresent()) {
            Post post = resultPost.get();
            if(Objects.equals(post.getCustomerEmail(), customUserDetails.getEmail())) {
                post.setPostTitle(dto.getPostTitle());
                post.setPostContent(dto.getPostContent());
                postRepository.save(post);
                List<GetPostImageRes> getPostImageResList = new ArrayList<>();
                Optional<List<PostImage>> resultPostImage = postImageRepository.findByPostIdx(post.getPostIdx());
                if(resultPostImage.isPresent()){
                    List<PostImage> postImageList = resultPostImage.get();
                    for(int i = 0; i < postImageList.size(); i++) {
                        postImageList.get(i).setPostImageUrl(fileNames.get(i));
                        postImageRepository.save(postImageList.get(i));
                        GetPostImageRes getPostImageRes = GetPostImageRes.builder()
                                .postImageIdx(postImageList.get(i).getPostImageIdx())
                                .postImageUrl(postImageList.get(i).getPostImageUrl())
                                .createdAt(postImageList.get(i).getCreatedAt())
                                .updatedAt(postImageList.get(i).getUpdatedAt())
                                .build();
                        getPostImageResList.add(getPostImageRes);
                    }
                }
                return UpdatePostRes.builder()
                        .postIdx(post.getPostIdx())
                        .customerEmail(post.getCustomerEmail())
                        .postTitle(post.getPostTitle())
                        .postContent(post.getPostContent())
                        .getPostImageRes(getPostImageResList)
                        .createdAt(post.getCreatedAt())
                        .updatedAt(post.getUpdatedAt())
                        .build();
            } else {
                throw new BaseException(BaseResponseMessage.POST_UPDATE_FAIL_INVALID_MEMBER);
            }
        } else {
            throw new BaseException(BaseResponseMessage.POST_UPDATE_FAIL);
        }
    }

    public void delete(CustomUserDetails customUserDetails, Long postIdx) throws BaseException{
        Optional<Post> result = postRepository.findById(postIdx);
        if(result.isPresent()){
            Post post = result.get();
            if(Objects.equals(post.getCustomerEmail(), customUserDetails.getEmail())){
              postRepository.deleteById(postIdx);
            } else {
                throw new BaseException(BaseResponseMessage.POST_DELETE_FAIL_INVALID_MEMBER);
            }
        } else {
            throw new BaseException(BaseResponseMessage.POST_DELETE_FAIL_NOT_FOUND);
        }
    }

    @Transactional
    public void adjustLike(CustomUserDetails customUserDetails, Long postIdx) throws BaseException{
        Optional<Post> resultPost = postRepository.findById(postIdx);
        if(resultPost.isPresent()){
            Optional<Customer> resultCustomer = customerRepository.findById(customUserDetails.getIdx());

            if(resultCustomer.isPresent()){
                Post post = resultPost.get();
                Customer customer = resultCustomer.get();
                Optional<PostLike> resultPostLike = postLikeRepository.findByCustomerIdxAndPostIdx(customUserDetails.getIdx(), postIdx);
                if(resultPostLike.isEmpty()){
                    post.setPostLikeCount(post.getPostLikeCount() + 1);
                    postRepository.save(post);
                    PostLike postLike = PostLike.builder()
                            .post(post)
                            .customer(customer)
                            .build();
                    postLikeRepository.save(postLike);
                } else {
                    post.setPostLikeCount(post.getPostLikeCount() - 1);
                    postRepository.save(post);
                    postLikeRepository.deleteByCustomerIdxAndPostIdx(customer.getCustomerIdx(), postIdx);
                }
            }else {
                throw new BaseException(BaseResponseMessage.POST_LIKE_FAIL_INVALID_MEMBER);
            }
        } else {
            throw new BaseException(BaseResponseMessage.POST_LIKE_FAIL_NOT_FOUND);
        }
    }
}
