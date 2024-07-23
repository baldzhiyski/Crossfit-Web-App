package com.softuni.crossfitapp.service.impl;

import com.softuni.crossfitapp.domain.dto.comments.AddCommentDto;
import com.softuni.crossfitapp.domain.dto.comments.CommentAdminPageDto;
import com.softuni.crossfitapp.domain.dto.comments.DisplayCommentDto;
import com.softuni.crossfitapp.domain.entity.Comment;
import com.softuni.crossfitapp.domain.entity.Training;
import com.softuni.crossfitapp.domain.entity.User;
import com.softuni.crossfitapp.domain.entity.enums.TrainingType;
import com.softuni.crossfitapp.exceptions.AccessOnlyForCoaches;
import com.softuni.crossfitapp.exceptions.ObjectNotFoundException;
import com.softuni.crossfitapp.repository.CommentRepository;
import com.softuni.crossfitapp.repository.TrainingRepository;
import com.softuni.crossfitapp.repository.UserRepository;
import com.softuni.crossfitapp.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {
    private CommentRepository commentRepository;
    private TrainingRepository trainingRepository;
    private UserRepository userRepository;

    private ModelMapper mapper;

    public CommentServiceImpl(CommentRepository commentRepository, TrainingRepository trainingRepository, UserRepository userRepository, ModelMapper mapper) {
        this.commentRepository = commentRepository;
        this.trainingRepository = trainingRepository;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public Comment addComment(AddCommentDto addCommentDto, TrainingType trainingType) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = this.userRepository.findByUsername(username).get();
        Training training = this.trainingRepository.findByTrainingType(trainingType).orElseThrow(() -> new ObjectNotFoundException("Not such training type !"));
        Comment comment = new Comment();
        comment.setTraining(training);
        comment.setAuthor(user);
        comment.setText(addCommentDto.getDescription());
        comment.setDate(LocalDate.now());
        comment.setLikes(0);
        comment.setDislikes(0);
        return this.commentRepository.saveAndFlush(comment);
    }

    @Override
    public List<DisplayCommentDto> getAllCommentsOnCurrentTrainingType(TrainingType trainingType) {
        return this.commentRepository
                .findAllByTraining_TrainingType(trainingType)
                .stream()
                .map(comment -> this.mapper.map(comment, DisplayCommentDto.class))
                .toList();
    }

    @Override
    public void cleanUpOldComments() {
        this.commentRepository.deleteAll();
    }

    @Override
    @Transactional
    public void likeComment(UUID commentId, String username) {
        Comment comment = this.commentRepository.findByUuid(commentId).orElseThrow(() -> new ObjectNotFoundException("Invalid comment id !"));
        User user = this.userRepository.findByUsername(username).orElseThrow(() -> new ObjectNotFoundException("No such user in the db!"));


        if(comment.getAuthor().equals(user)) return;

        if(!comment.getLikedBy().contains(user)){
            comment.setLikes(comment.getLikes() + 1);
            comment.getLikedBy().add(user);

            if (comment.getDislikedBy().contains(user)) {
                comment.setDislikes(comment.getDislikes() - 1);
                comment.getDislikedBy().remove(user);
            }
        }
        this.commentRepository.saveAndFlush(comment);
    }

    @Override
    @Transactional
    public void dislike(UUID commentId, String username) {
        Comment comment = this.commentRepository.findByUuid(commentId)
                .orElseThrow(() -> new ObjectNotFoundException("Invalid comment id !"));
        User user = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new ObjectNotFoundException("No such user in the db!"));

        if(comment.getAuthor().equals(user)) return;

        if (!comment.getDislikedBy().contains(user)) {
            comment.getDislikedBy().add(user);
            comment.setDislikes(comment.getDislikes() + 1);

            if (comment.getLikedBy().contains(user)) {
                comment.getLikedBy().remove(user);
                comment.setLikes(comment.getLikes() - 1);
            }
        }

        this.commentRepository.saveAndFlush(comment);
    }

    @Override
    public Comment getById(UUID commentId) {
        return this.commentRepository.findByUuid(commentId).orElseThrow(()->new ObjectNotFoundException("Invalid commend id"));
    }

    @Override
    @Transactional
    public void deleteComment(UUID commentId, String username) {
        Comment comment = this.commentRepository.findByUuid(commentId).orElseThrow(() -> new ObjectNotFoundException("Invalid comment id !"));
        User user = this.userRepository.findByUsername(username).orElseThrow(() -> new ObjectNotFoundException("No such user in the db!"));
        if(comment.getAuthor().equals(user)){
            comment.getLikedBy().clear();
            comment.getDislikedBy().clear();
            comment.setAuthor(null);
            comment.setTraining(null);
           this.commentRepository.saveAndFlush(comment);
           this.commentRepository.delete(comment);
        }else{
            throw new AccessOnlyForCoaches("Not acceptable move !");
        }
    }

    @Override
    public List<CommentAdminPageDto> displayAllComments() {
        return this.commentRepository.findAll()
                .stream()
                .map(comment -> this.mapper.map(comment,CommentAdminPageDto.class))
                .collect(Collectors.toList());
    }


    // TODO : Add also an email here
    @Override
    public void deleteCommentAdmin(UUID commentUUID, String authorUsername) {
        Comment comment = this.commentRepository.findByUuid(commentUUID).orElseThrow(() -> new ObjectNotFoundException("Invalid comment id !"));
        User user = this.userRepository.findByUsername(authorUsername).orElseThrow(() -> new ObjectNotFoundException("No such user in the db!"));

        comment.getLikedBy().clear();
        comment.getDislikedBy().clear();
        comment.setAuthor(null);
        comment.setTraining(null);
        this.commentRepository.saveAndFlush(comment);
        this.commentRepository.delete(comment);
    }


}
