package org.example.expert.domain.comment.controller;

import org.example.expert.domain.comment.dto.request.CommentSaveRequest;
import org.example.expert.domain.comment.dto.response.CommentSaveResponse;
import org.example.expert.domain.comment.service.CommentService;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.when;

@ExtendWith(MockitoExtension.class)
public class CommentControllerTest {
    @InjectMocks
    CommentController commentController;

    @Mock
    private CommentService commentService;

    @Test
    public void 코멘트_작성_성공() {
        //given
        long todoId = 1L;

        AuthUser authUser = new AuthUser(1L, "email", UserRole.USER);
        User user = new User("email", "12321", UserRole.USER);
        CommentSaveRequest saveRequest = new CommentSaveRequest("피곤해");
        UserResponse userResponse = new UserResponse(1L, "email@email.com");
        CommentSaveResponse commentSaveRequest = new CommentSaveResponse(1L, "피곤해", userResponse);
        Todo todo = new Todo("title", "contents", "icy", user);
        ReflectionTestUtils.setField(todo, "id", todoId);

        //when
        when(commentService.saveComment(authUser, todo.getId(), saveRequest)).thenReturn(commentSaveRequest);
        ResponseEntity<CommentSaveResponse> response = commentController.saveComment(authUser, todo.getId(), saveRequest);

        //then
        assertNotNull(saveRequest);
        assertEquals("피곤해", response.getBody().getContents());
        assertEquals(1L, response.getBody().getId());

    }

}
