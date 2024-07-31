package com.softuni.crossfitapp.web;

import com.softuni.crossfitapp.domain.dto.comments.CommentAdminPageDto;
import com.softuni.crossfitapp.domain.dto.users.UserAdminPageDto;
import com.softuni.crossfitapp.service.CommentService;
import com.softuni.crossfitapp.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
public class AdminController {

    private UserService userService;
    private CommentService commentService;

    public AdminController(UserService userService, CommentService commentService) {
        this.userService = userService;
        this.commentService = commentService;
    }

    @ModelAttribute("totalBoughtMemberships")
    public Long totalBoughMemberships(){
        return this.userService.getTotalMoney();
    }

    @ModelAttribute("totalUserAccounts")
    public Long getTotalNumAcc(){
        return this.userService.numberAllActiveUsers();
    }


    @ModelAttribute("users")
    public List<UserAdminPageDto> displayUsers(){
        return userService.displayAllUsersAcc();
    }

    @ModelAttribute("comments")
    public List<CommentAdminPageDto> displayComments(){
        return commentService.displayAllComments();
    }

    @GetMapping("/profiles-dashboard")
    public String dashboard(){
        return "dashboard";
    }

    @PatchMapping("/disableAcc/{accountUUID}")
    public String disableAcc(@PathVariable UUID accountUUID){
        this.userService.enableOrDisableAcc(accountUUID,"disable");

        return "redirect:/profiles-dashboard";
    }

    @PatchMapping("/enableAcc/{accountUUID}")
    public String enableAcc(@PathVariable UUID accountUUID){
        this.userService.enableOrDisableAcc(accountUUID,"enable");

        return "redirect:/profiles-dashboard";
    }

    @DeleteMapping("/deleteComment/{commentUUID}/{authorUsername}")
    public String deleteComment(@PathVariable UUID commentUUID, @PathVariable String authorUsername){
        this.commentService.deleteCommentAdmin(commentUUID,authorUsername);

        return "redirect:/profiles-dashboard";
    }

    // TODO : Check with preauthorize if the logged user has role admin
}
