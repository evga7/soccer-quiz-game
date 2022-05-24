package pyo.quizgame.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pyo.quizgame.domain.*;
import pyo.quizgame.dto.UserCommentDto;
import pyo.quizgame.dto.UserPostDto;
import pyo.quizgame.service.FrontUserService;
import pyo.quizgame.service.UserLikeService;
import pyo.quizgame.service.UserPostService;

import javax.validation.Valid;
import java.util.*;

@Controller
@RequiredArgsConstructor
public class UserPostController {

    private final UserPostService userPostService;
    private final UserLikeService userLikeService;
    private final FrontUserService frontUserService;

    @Operation(summary = "유저 게시물 작성")
    @PostMapping("/front-user/user-post")
    public String userPost(@Valid UserPostDto userPostDto, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors())
            return "addPost";
        String nickName = userPostDto.getNickName();
        UserPost userPost = UserPost.builder()
                .nickName(userPostDto.getNickName())
                .title(userPostDto.getTitle())
                .content(userPostDto.getContent())
                .build();
        redirectAttributes.addAttribute("nickName", nickName);
        userPostService.save(userPost);
        frontUserService.upPostCount(userPost.getNickName());
        return "redirect:/user-boards";
    }


    @Operation(summary = "게시물 수정하기")
    @PostMapping("/front-user/user-post/edit-post")
    public String userEditPost(UserPostDto userPostDto, Long postId, RedirectAttributes redirectAttributes,Model model) {

        String title = userPostDto.getTitle();
        String content = userPostDto.getContent();
        Optional<UserPost> userPostByPostId = userPostService.getUserPostByPostId(postId);
        redirectAttributes.addAttribute("nickName", userPostDto.getNickName());
        if (titleWrongChk(title)|| contentWrongChk(content))
        {
            Map<String, String> errors = new HashMap<>();
            if (titleWrongChk(title))
                errors.put("titleLength", "제목은 2~25자 로 작성해 주세요.");
            if (contentWrongChk(content))
                errors.put("contentLength", "내용은 2~500자 로 작성해 주세요.");
            model.addAttribute("errors", errors);
            model.addAttribute("beforeContent", content);
            model.addAttribute("beforeTitle", title);
            model.addAttribute("postId", postId);
            redirectAttributes.addAttribute("postId", postId);
            return "editPost";
        }
        userPostByPostId.get().setTitle(userPostDto.getTitle());
        userPostByPostId.get().setContent(userPostDto.getContent());
        userPostService.save(userPostByPostId.get());
        return "redirect:/user-boards";
    }

    @Operation(summary = "유저 게시판 가져오기")
    @GetMapping("/user-boards")
    public String getUserPostPage(Model model, @PageableDefault(size = 10, sort = "id",direction = Sort.Direction.DESC) Pageable pageable,
                                  @RequestParam(required = false, defaultValue = "") String searchText,@RequestParam String nickName) {
        Page<UserPost> userBoards = getUserBoards(searchText, pageable, nickName);
        int startPage = 1;
        int totalPages = userBoards.getTotalPages();
        int currentPageNumber = userBoards.getPageable().getPageNumber()+1;
        model.addAttribute("currentPageNumber",currentPageNumber);
        model.addAttribute("startPage", startPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("userBoards", userBoards);
        return "userBoards";
    }

    @Operation(summary = "게시판 게시글 조회")
    @GetMapping("/front-user/user-post/{postId}")
    public String getUserPost(@PathVariable Long postId, Model model, @RequestParam String nickName,
                              @PageableDefault(size = 10,sort = "id",direction = Sort.Direction.DESC)Pageable pageable)
    {
        Optional<UserPost> userPost = userPostService.getUserPostByPostId(postId);
        Optional<FrontUserInfo> user = frontUserService.findUser(nickName);
        Optional<UserLike> alreadyLike = userLikeService.isAlreadyLike(user.get(), userPost.get());
        UserPostDto userPostDto = createUserPostWithCommentDto(userPost.get());
        final Page<UserCommentDto> page = convertListToPage(pageable, userPostDto);
        String likeCheck="0";
        boolean present = alreadyLike.isPresent();
        if (present) {
            likeCheck="1";
        }
        model.addAttribute("likeCheck", likeCheck);
        userPostService.updateView(postId);
        model.addAttribute("post", userPost.get());
        model.addAttribute("postId", userPost.get().getId());
        model.addAttribute("postTitle", userPost.get().getTitle());
        model.addAttribute("postContent", userPost.get().getContent());
        model.addAttribute("commentCount", userPost.get().getCommentCount());
        model.addAttribute("comments", page);
        model.addAttribute("likeCount", userPost.get().getLikeCount());
        int startPage = 1;
        int totalPages = page.getTotalPages();
        int currentPageNumber = page.getPageable().getPageNumber()+1;
        model.addAttribute("postNickName", userPost.get().getNickName());
        model.addAttribute("currentPageNumber",currentPageNumber);
        model.addAttribute("startPage", startPage);
        model.addAttribute("totalPages", totalPages);
        return "posts";
    }


    @Operation(summary = "게시글 작성 페이지")
    @GetMapping("/front-user/user-post/addPost")
    public String addPost(Model model,@RequestParam String nickName)
    {
        model.addAttribute("userPostDto", new UserPostDto());
        return "addPost";
    }

    @Operation(summary = "게시글 수정 페이지")
    @GetMapping("/front-user/user-post/editPost")
    public String editPost(Model model,@RequestParam String nickName,@RequestParam Long postId)
    {
        Optional<UserPost> userPostByPostId = userPostService.getUserPostByPostId(postId);
        model.addAttribute("beforeTitle",userPostByPostId.get().getTitle());
        model.addAttribute("beforeContent",userPostByPostId.get().getContent());
        model.addAttribute("postId",postId);
        model.addAttribute("userPostDto", new UserPostDto());
        return "editPost";
    }

    //dto 사용해서 return 하는함수 일단 jsonmange로 바꿨음 // 다시바꿈 3.8일날;;
    @Operation(summary = "게시글 삭제하기")
    @PostMapping("/front-user/user-post/del-post")
    @ResponseBody
    public String delPost(Long id,String nickName)
    {
        Optional<UserPost> userPostByPostId = userPostService.getUserPostByPostId(id);
        if (userPostByPostId.isPresent()) {
            if (userPostByPostId.get().getNickName().compareTo(nickName) != 0)
                return nickName;
            if (userPostByPostId.get().getCommentCount()>0)
                return nickName;
        }
        frontUserService.downPostCount(nickName);
        List<UserLike> userLikeByPostId = userLikeService.getUserLikeByPostId(userPostByPostId.get());
        for (UserLike userLike : userLikeByPostId) {
            userLikeService.delLike(userLike);
        }
        userPostService.deletePost(userPostByPostId.get());
        return nickName;
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
    private boolean titleWrongChk(String title) {
        return title.length() < 2 || title.length() > 25;
    }

    private boolean contentWrongChk(String content) {
        return content.length() < 2 || content.length() > 25;
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

    //너무 비효율적이다..
    public Page<UserPost> getUserBoards(String searchText,Pageable pageable,String nickName)
    {
        List<UserPost> oriUserBoards = userPostService.getUserBoardList(searchText);
        Optional<FrontUserInfo> user = frontUserService.findUser(nickName);
        Set<UserBlock> userBlocks = user.get().getUserBlocks();
        Set<String> userBlocksSet = new HashSet<>();
        for (UserBlock userBlock : userBlocks) {
            userBlocksSet.add(userBlock.getBlockedNickName());
        }
        List<UserPost> userPostList = new ArrayList<>();
        for (UserPost oriUserBoard : oriUserBoards) {
            if (userBlocksSet.contains(oriUserBoard.getNickName()))continue;
            userPostList.add(oriUserBoard);
        }
        final int start = (int) pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), userPostList.size());
        final Page<UserPost> page = new PageImpl<>(userPostList.subList(start, end), pageable, userPostList.size());
        return page;
    }


}
