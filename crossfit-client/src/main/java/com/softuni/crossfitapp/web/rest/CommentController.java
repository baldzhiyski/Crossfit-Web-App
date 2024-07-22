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
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class CommentController {

    private CommentService commentService;
    private ModelMapper mapper;

    public CommentController(CommentService commentService, ModelMapper mapper) {
        this.commentService = commentService;
        this.mapper = mapper;
    }

    @GetMapping("/workouts/details/{trainingType}/comments")
    public ResponseEntity<List<DisplayCommentDto>> getAllComments(@PathVariable TrainingType trainingType){
        List<DisplayCommentDto> allCommentsOnCurrentTrainingType = commentService.getAllCommentsOnCurrentTrainingType(trainingType);

        return ResponseEntity.ok(allCommentsOnCurrentTrainingType);
    }

    @PostMapping("/workouts/details/{trainingType}/comment/like/{commentId}")
    public ResponseEntity<DisplayCommentDto> likeComment(
            @PathVariable TrainingType trainingType,
            @PathVariable UUID commentId,
            // Changed to String if `TrainingType` is an enum
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
            @PathVariable String trainingType,
            @PathVariable UUID commentId,
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
            @PathVariable TrainingType trainingType,
            @RequestBody AddCommentDto addCommentDto) {
        // Add the comment using the service
        Comment newComment = this.commentService.addComment(addCommentDto, trainingType);

        // Map the new comment to a DTO
        DisplayCommentDto response = mapper.map(newComment, DisplayCommentDto.class);

        // Return the updated comment data
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/workouts/details/{trainingType}/comment/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable TrainingType trainingType,
            @PathVariable UUID commentId,
            @AuthenticationPrincipal UserDetails userDetails) {
        commentService.deleteComment(commentId, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
