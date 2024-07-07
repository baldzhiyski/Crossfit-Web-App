package com.softuni.crossfitapp.service.schedulers;

import com.softuni.crossfitapp.service.CommentService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CommentScheduler {

    private CommentService commentService;

    public CommentScheduler(CommentService commentService) {
        this.commentService = commentService;
    }

    @Scheduled(cron = "0 0 0 */2 * *") // Cron expression for every 2 weeks
    public void cleanUpOldComments(){
        // Implement logic to clean up old comments using the commentService
        commentService.cleanUpOldComments();
    }
}
