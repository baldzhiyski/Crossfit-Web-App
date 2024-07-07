package com.softuni.crossfitapp.service.impl;

import com.softuni.crossfitapp.domain.dto.comments.AddCommentDto;
import com.softuni.crossfitapp.domain.dto.comments.DisplayCommentDto;
import com.softuni.crossfitapp.domain.entity.Comment;
import com.softuni.crossfitapp.domain.entity.Training;
import com.softuni.crossfitapp.domain.entity.User;
import com.softuni.crossfitapp.domain.entity.enums.TrainingType;
import com.softuni.crossfitapp.exceptions.ObjectNotFoundException;
import com.softuni.crossfitapp.repository.CommentRepository;
import com.softuni.crossfitapp.repository.TrainingRepository;
import com.softuni.crossfitapp.repository.UserRepository;
import com.softuni.crossfitapp.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

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
    public void addComment(AddCommentDto addCommentDto, TrainingType trainingType) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = this.userRepository.findByUsername(username).get();
        Training training = this.trainingRepository.findByTrainingType(trainingType).orElseThrow(() -> new ObjectNotFoundException("Not such training type !"));
        Comment comment = new Comment();
        comment.setTraining(training);
        comment.setAuthor(user);
        comment.setText(addCommentDto.getDescription());
        comment.setDate(LocalDate.now());
        this.commentRepository.save(comment);
    }

    @Override
    public List<DisplayCommentDto> getAllCommentsOnCurrentTrainingType(TrainingType trainingType) {
        return this.commentRepository
                .findAllByTraining_TrainingType(trainingType)
                .stream()
                .map(comment -> this.mapper.map(comment, DisplayCommentDto.class))
                .toList();
    }
}
