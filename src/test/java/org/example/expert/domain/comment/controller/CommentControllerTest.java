//package org.example.expert.domain.comment.controller;
//
//import org.example.expert.domain.comment.dto.request.CommentSaveRequest;
//import org.example.expert.domain.comment.dto.response.CommentResponse;
//import org.example.expert.domain.comment.dto.response.CommentSaveResponse;
//import org.example.expert.domain.comment.repository.CommentRepository;
//import org.example.expert.domain.comment.service.CommentService;
//import org.example.expert.domain.common.dto.AuthUser;
//import org.example.expert.domain.todo.entity.Todo;
//import org.example.expert.domain.user.dto.response.UserResponse;
//import org.example.expert.domain.user.entity.User;
//import org.example.expert.domain.user.enums.UserRole;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.test.util.ReflectionTestUtils;
//
//@ExtendWith(MockitoExtension.class)
//public class CommentControllerTest {
//    @Mock
//    private CommentRepository commentRepository;
//
//    @InjectMocks
//    private CommentService commentService;
//
//    @Test
//    public void 코멘트_작성_성공() {
//        //given
//        long userId = 1L;
//        AuthUser authUser = new AuthUser(1L, "email", UserRole.USER);
//        User user = new User("email", "12321", UserRole.USER);
//        Todo todo = new Todo("title", "contents", "icy", user)
//        ReflectionTestUtils.setField(user, "id", userId);
//        ReflectionTestUtils.setField(todo, "id", 1L);
//        CommentSaveRequest commentSaveRequest = new CommentSaveRequest("contents");
//
//        //when
//        CommentSaveResponse commentResponse = commentService.saveComment(authUser, todo.getId(), commentSaveRequest);
//
//        //then
//
//
//    }
//
//}
