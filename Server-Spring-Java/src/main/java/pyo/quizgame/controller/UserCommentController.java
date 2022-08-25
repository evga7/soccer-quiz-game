package pyo.quizgame.controller;


import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import pyo.quizgame.domain.FrontUserInfo;
import pyo.quizgame.domain.UserComment;
import pyo.quizgame.domain.UserLike;
import pyo.quizgame.domain.UserPost;
import pyo.quizgame.dto.UserCommentDto;
import pyo.quizgame.dto.UserPostDto;
import pyo.quizgame.service.FrontUserService;
import pyo.quizgame.service.UserCommentService;
import pyo.quizgame.service.UserLikeService;
import pyo.quizgame.service.UserPostService;

import javax.validation.Valid;
import java.util.*;

@Controller
@RequiredArgsConstructor
public class UserCommentController {
    private final UserCommentService userCommentService;
    private final UserPostService userPostService;
    private final UserLikeService userLikeService;
    private final FrontUserService frontUserService;


    @Operation(summary = "댓글 쓰기")
    @PostMapping("/front-user/user-post/comment")
    public String writeComment(Long id, String nickName, String content, Model model,
                               @PageableDefault(size = 10)Pageable pageable)
    {
        UserPost userPost = userPostService.getUserPostByPostId(id).get();
        Map<String, String> errors = new HashMap<>();
        UserComment userComment = makeComment(nickName, content, userPost);
        Long commentCount = userPost.getCommentCount();

        commentCount = commentChk(id, content, model, userPost, errors, userComment, commentCount);
        UserPostDto userPostWithCommentDto = createUserPostWithCommentDto(userPost);
        Page<UserCommentDto> userCommentDtos = convertListToPage(pageable, userPostWithCommentDto);
        Long postId = userPost.getId();
        String postNickName = userPost.getNickName();

        int startPage = 1;
        int totalPages = userCommentDtos.getTotalPages();
        int currentPageNumber = userCommentDtos.getPageable().getPageNumber()+1;
        Optional<FrontUserInfo> user = frontUserService.findUser(nickName);
        Optional<UserLike> alreadyLike = userLikeService.isAlreadyLike(user.get(), userPost);
        Long likeCount = userPost.getLikeCount();
        String likeCheck="0";
        if (alreadyLike.isPresent()) {
            likeCheck="1";
        }
        commentModel(model, commentCount, userCommentDtos, postId, postNickName, startPage, totalPages, currentPageNumber, likeCount, likeCheck);
        frontUserService.upCommentCount(nickName);
        return "posts :: #commentTable";
    }


    @Operation(summary = "댓글 수정하기")
    @PutMapping("/front-user/user-post/comment")
    public String editComment(Long id, String nickName, String content, Model model,Long postId,
                               @PageableDefault(size = 10)Pageable pageable)
    {
        if (!commentWrongChk(content)) {
            Optional<UserComment> userCommentById = userCommentService.getUserCommentById(id);
            userCommentById.get().setContent(content);
            userCommentService.save(userCommentById.get());
        }
        Optional<UserPost> userPostByPostId = userPostService.getUserPostByPostId(postId);
        UserPostDto userPostWithCommentDto = createUserPostWithCommentDto(userPostByPostId.get());
        Page<UserCommentDto> userCommentDtos = convertListToPage(pageable, userPostWithCommentDto);
        model.addAttribute("comments",userCommentDtos);

        return "posts :: #comment-content";
    }

    @Operation(summary = "댓글 삭제하기")
    @DeleteMapping("/front-user/user-post/comment")
    public String delUserComment(Long id,String nickName,Long postId,Model model,@PageableDefault(size = 10)Pageable pageable)
    {
        Optional<UserComment> userCommentById = userCommentService.getUserCommentById(id);
        Optional<UserPost> userPostByPostId = userPostService.getUserPostByPostId(postId);
        if (userCommentById.isPresent()) {
            userCommentService.delete(userCommentById.get());
            frontUserService.downCommentCount(nickName);
            userPostService.downCommentCount(postId);
            userPostByPostId.get().deleteComment(userCommentById.get());
        }
        Long commentCount = userPostByPostId.get().getCommentCount();
        UserPostDto userPostWithCommentDto = createUserPostWithCommentDto(userPostByPostId.get());
        Page<UserCommentDto> userCommentDtos = convertListToPage(pageable, userPostWithCommentDto);
        int startPage = 1;
        int totalPages = userCommentDtos.getTotalPages();
        int currentPageNumber = userCommentDtos.getPageable().getPageNumber()+1;
        Long likeCount = userPostByPostId.get().getLikeCount();
        Optional<FrontUserInfo> user = frontUserService.findUser(nickName);
        Optional<UserLike> alreadyLike = userLikeService.isAlreadyLike(user.get(), userPostByPostId.get());
        String likeCheck="0";
        if (alreadyLike.isPresent()) {
            likeCheck="1";
        }
        commentModel(model, --commentCount, userCommentDtos, postId, userPostByPostId.get().getNickName(), startPage, totalPages, currentPageNumber, likeCount, likeCheck);
        return "posts :: #commentTable";
    }

    private boolean commentWrongChk(String content) {
        return content.length() < 1 || content.length() > 45;
    }

    private void commentModel(Model model, Long commentCount, Page<UserCommentDto> userCommentDtos, Long postId, String postNickName, int startPage, int totalPages, int currentPageNumber, Long likeCount, String likeCheck) {
        model.addAttribute("commentCount", commentCount);
        model.addAttribute("comments", userCommentDtos);
        model.addAttribute("postId", postId);
        model.addAttribute("currentPageNumber", currentPageNumber);
        model.addAttribute("startPage", startPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("postNickName", postNickName);
        model.addAttribute("likeCount", likeCount);
        model.addAttribute("likeCheck", likeCheck);
    }

    private Long commentChk(Long id, String content, Model model, UserPost userPost, Map<String, String> errors, UserComment userComment, Long commentCount) {
        if (content.length()>45)
            putError(errors, "contentLength", "댓글은 45자 이내로 작성해주세요.");
        if (StringUtils.isEmpty(content))
        {
            putError(errors, "content", "댓글을 입력해주세요.");
        }
        if (errors.isEmpty())
        {
            commentCount++;
            userCommentService.save(userComment);
            userPost.addComment(userComment);
            userPostService.upCommentCount(id);
        }
        else
            model.addAttribute("errors", errors);
        return commentCount;
    }

    private void putError(Map<String, String> errors, String contentLength, String v) {
        errors.put(contentLength, v);
    }
    private UserComment makeComment(String nickName, String content, UserPost userPost) {
        UserComment userComment = new UserComment();
        userComment.setNickName(nickName);
        userComment.setContent(content);
        userComment.setUserPost(userPost);
        return userComment;
    }
    private Page<UserCommentDto> convertListToPage(Pageable pageable, UserPostDto userPost) {
        List<UserCommentDto> comments = userPost.getComments();
        sortCommentList(comments);
        final int start = (int) pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), comments.size());
        final Page<UserCommentDto> page = new PageImpl<>(comments.subList(start, end), pageable, comments.size());
        return page;
    }
    private void sortCommentList(List<UserCommentDto> comments) {
        comments.sort(new Comparator<UserCommentDto>() {
            @Override
            public int compare(UserCommentDto t1, UserCommentDto t2) {
                return Long.compare(t2.getId(), t1.getId());
            }
        });
    }
    private BindingResult checkValid(@Valid UserCommentDto userCommentDto, BindingResult bindingResult)
    {
        return bindingResult;
    }

    private UserPostDto createUserPostWithCommentDto(UserPost userPost) {
        UserPostDto userPostDto = new UserPostDto();
        userPostDto.setNickName(userPost.getNickName());
        userPostDto.setContent(userPost.getContent());
        userPostDto.setTitle(userPost.getTitle());
        userPostDto.setCreatedAt(userPost.getCreatedDate());
        userPostDto.setModifiedAt(userPost.getModifiedDate());
        List<UserCommentDto> userComments = new ArrayList<>();
        for (UserComment userComment : userPost.getUserComments()) {
            UserCommentDto userCommentDto= new UserCommentDto();
            userCommentDto.setContent(userComment.getContent());
            userCommentDto.setNickName(userComment.getNickName());
            userCommentDto.setId(userComment.getId());
            userCommentDto.setCreatedDate(userComment.getCreatedDate());
            userCommentDto.setModifiedDate(userComment.getModifiedDate());
            userComments.add(userCommentDto);
        }
        userPostDto.setComments(userComments);
        return userPostDto;
    }
}
