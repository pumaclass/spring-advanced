package org.example.expert.domain.comment.service;

import org.example.expert.client.WeatherClient;
import org.example.expert.domain.comment.dto.request.CommentSaveRequest;
import org.example.expert.domain.comment.dto.response.CommentResponse;
import org.example.expert.domain.comment.dto.response.CommentSaveResponse;
import org.example.expert.domain.comment.entity.Comment;
import org.example.expert.domain.comment.repository.CommentRepository;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.repository.TodoRepository;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;
    @Mock
    private TodoRepository todoRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private WeatherClient weatherClient;
    @InjectMocks
    private CommentService commentService;

    @Test
    public void comment_등록_중_할일을_찾지_못해_에러가_발생한다() {
        // given
        long todoId = 1;
        CommentSaveRequest request = new CommentSaveRequest("contents");
        AuthUser authUser = new AuthUser(1L, "email", UserRole.USER);

        given(todoRepository.findById(anyLong())).willReturn(Optional.empty());

        // when
        InvalidRequestException exception = assertThrows(InvalidRequestException.class, () -> {
            commentService.saveComment(authUser, todoId, request);
        });

        // then
        assertEquals("Todo not found", exception.getMessage());
    }

    @Test
    public void comment를_정상적으로_등록한다() {
        // given
        long todoId = 1;
        CommentSaveRequest request = new CommentSaveRequest("contents");
        AuthUser authUser = new AuthUser(1L, "email", UserRole.USER);
        User user = User.fromAuthUser(authUser);
        Todo todo = new Todo("title", "title", "contents", user);
        Comment comment = new Comment(request.getContents(), user, todo);

        given(todoRepository.findById(anyLong())).willReturn(Optional.of(todo));
        given(commentRepository.save(any())).willReturn(comment);

        // when
        CommentSaveResponse result = commentService.saveComment(authUser, todoId, request);

        // then
        assertNotNull(result);
    }

    @Test
    public void 코멘트를_정상적으로_조회한다() {
        //given
        long userId = 2L;
        long todoId = 1L;
        User user = new User("email", "12321", UserRole.USER);
        ReflectionTestUtils.setField(user, "id", userId);
        given(weatherClient.getTodayWeather()).willReturn("icy");
        Todo todo = new Todo("title", "contents", weatherClient.getTodayWeather(), user);
        ReflectionTestUtils.setField(todo, "id", todoId);
        Comment comment1 = new Comment("contents", user, todo);
        ReflectionTestUtils.setField(comment1, "id", 1L);
        Comment comment2 = new Comment("contents2", user, todo);
        ReflectionTestUtils.setField(comment2, "id", 2L);
        List<Comment> comments = Arrays.asList(comment1, comment2);

        given(commentRepository.findByTodoIdWithUser(anyLong())).willReturn(comments);
        //when

        List<CommentResponse> savedComments = commentService.getComments(todoId);

        //then
        assertNotNull(savedComments);
        assertEquals(1, savedComments.get(0).getId());
        assertEquals(2, savedComments.get(1).getId());
        assertEquals("contents", savedComments.get(0).getContents());
        assertEquals("contents2", savedComments.get(1).getContents());
    }

}