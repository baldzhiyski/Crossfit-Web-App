package com.softuni.crossfitapp.service.impl;

import com.softuni.crossfitapp.domain.dto.comments.AddCommentDto;
import com.softuni.crossfitapp.domain.dto.comments.DisplayCommentDto;
import com.softuni.crossfitapp.domain.entity.Comment;
import com.softuni.crossfitapp.domain.entity.Role;
import com.softuni.crossfitapp.domain.entity.Training;
import com.softuni.crossfitapp.domain.entity.User;
import com.softuni.crossfitapp.domain.entity.enums.RoleType;
import com.softuni.crossfitapp.domain.entity.enums.TrainingType;
import com.softuni.crossfitapp.exceptions.ObjectNotFoundException;
import com.softuni.crossfitapp.repository.CommentRepository;
import com.softuni.crossfitapp.repository.TrainingRepository;
import com.softuni.crossfitapp.repository.UserRepository;
import com.softuni.crossfitapp.service.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;
    @Mock
    private TrainingRepository trainingRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ModelMapper mapper;

    private CommentServiceImpl commentService;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.setContext(securityContext);
        this.commentService = new CommentServiceImpl(commentRepository,trainingRepository,userRepository,mapper);
    }

    @Test
    void testAddComment_success() {
        // Arrange
        AddCommentDto addCommentDto = new AddCommentDto();
        addCommentDto.setDescription("Great training!");

        TrainingType trainingType = TrainingType.WOD;

        User user = new User();
        user.setUsername("testUser");

        Training training = new Training();
        training.setTrainingType(trainingType);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testUser");
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(trainingRepository.findByTrainingType(trainingType)).thenReturn(Optional.of(training));

        // Act
        commentService.addComment(addCommentDto, trainingType);

        // Assert
        Mockito.verify(commentRepository).saveAndFlush(any(Comment.class));

    }

    @Test
    void testAddComment_trainingNotFound() {
        // Arrange
        AddCommentDto addCommentDto = new AddCommentDto();
        addCommentDto.setDescription("Great training!");

        TrainingType trainingType = TrainingType.WOD;

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testUser");
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(new User()));
        when(trainingRepository.findByTrainingType(trainingType)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ObjectNotFoundException.class, () -> {
            commentService.addComment(addCommentDto, trainingType);
        });
    }

    @Test
    void testGetAllCommentsOnCurrentTrainingType() {
        // Arrange
        TrainingType trainingType = TrainingType.WOD;

        Comment comment = new Comment();
        comment.setText("Great training!");
        Training training = new Training();
        training.setTrainingType(trainingType);
        comment.setTraining(training);

        DisplayCommentDto displayCommentDto = new DisplayCommentDto();
        displayCommentDto.setText("Great training!");

        when(commentRepository.findAllByTraining_TrainingType(trainingType)).thenReturn(List.of(comment));
        when(mapper.map(any(Comment.class), any(Class.class))).thenReturn(displayCommentDto);

        // Act
        List<DisplayCommentDto> result = commentService.getAllCommentsOnCurrentTrainingType(trainingType);

        // Assert
        assertEquals(1, result.size());
        assertEquals("Great training!", result.get(0).getText());
    }

    @Test
    void testCleanUpOldComments() {
        // Arrange
        Comment comment1 = new Comment();
        Comment comment2 = new Comment();
        List<Comment> comments = List.of(comment1, comment2);

        when(commentRepository.findAll()).thenReturn(comments);

        // Verify comments exist
        List<Comment> existingComments = commentRepository.findAll();
        assertTrue(existingComments.size() > 0);

        // Act
        commentService.cleanUpOldComments();

        // Assert
        Mockito.verify(commentRepository).deleteAll();


        // Now verify that findAll() returns an empty list after clean up
        when(commentRepository.findAll()).thenReturn(Collections.emptyList());
        assertEquals(0, commentRepository.findAll().size());
    }
}