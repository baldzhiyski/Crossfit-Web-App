package com.softuni.crossfitapp.web.rest;

import com.softuni.crossfitapp.domain.dto.comments.AddCommentDto;
import com.softuni.crossfitapp.domain.dto.comments.DisplayCommentDto;
import com.softuni.crossfitapp.domain.entity.Comment;
import com.softuni.crossfitapp.domain.entity.enums.TrainingType;
import com.softuni.crossfitapp.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class CommentController {

    private CommentService commentService;
    private ModelMapper mapper;

    public CommentController(CommentService commentService, ModelMapper mapper) {
        this.commentService = commentService;
        this.mapper = mapper;
    }

    @PostMapping("/workouts/details/{trainingType}/comment/like/{commentId}")
    public ResponseEntity<DisplayCommentDto> likeComment(
            @PathVariable UUID commentId,
            @PathVariable String trainingType, // Changed to String if `TrainingType` is an enum
            @AuthenticationPrincipal UserDetails userDetails) {
            // Call the service method to like the comment
           commentService.likeComment(commentId, userDetails.getUsername());
            Comment byId = this.commentService.getById(commentId);
            // Return the updated comment data
            DisplayCommentDto response = mapper.map(byId, DisplayCommentDto.class);
            return ResponseEntity.ok(response);
    }

    @PostMapping("/workouts/details/{trainingType}/comment/dislike/{commentId}")
    public ResponseEntity<DisplayCommentDto> dislikeComment(
            @PathVariable UUID commentId,
            @PathVariable String trainingType, // Changed to String if `TrainingType` is an enum
            @AuthenticationPrincipal UserDetails userDetails) {
        // Call the service method to like the comment
        commentService.dislike(commentId, userDetails.getUsername());
        Comment byId = this.commentService.getById(commentId);
        // Return the updated comment data
        DisplayCommentDto response = mapper.map(byId, DisplayCommentDto.class);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/workouts/details/comment/{trainingType}")
    public ResponseEntity<DisplayCommentDto> postComment(
            @PathVariable String trainingType,
            @RequestBody AddCommentDto addCommentDto) {
        // Add the comment using the service
        Comment newComment = this.commentService.addComment(addCommentDto, TrainingType.valueOf(trainingType));

        // Map the new comment to a DTO
        DisplayCommentDto response = mapper.map(newComment, DisplayCommentDto.class);

        // Return the updated comment data
        return ResponseEntity.ok(response);
    }


}
