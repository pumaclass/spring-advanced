package org.example.expert.domain.todo.service;

import org.example.expert.client.WeatherClient;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.todo.dto.request.TodoSaveRequest;
import org.example.expert.domain.todo.dto.response.TodoResponse;
import org.example.expert.domain.todo.dto.response.TodoSaveResponse;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TodoServiceTest {
    @Mock
    private TodoRepository todoRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private WeatherClient weatherClient;

    @InjectMocks
    private TodoService todoService;

    @Test
    public void 정상동작하는_일정_조회() {
        // given
        long todoId = 1L;
        long userId = 2L;
        User user = new User("email", "12321", UserRole.USER);
        ReflectionTestUtils.setField(user, "id", userId);
        Todo todo = new Todo("title", "contents", "icy", user);
        ReflectionTestUtils.setField(todo, "id", todoId);
        given(todoRepository.findByIdWithUser(anyLong())).willReturn(Optional.of(todo));
        // when
        TodoResponse todoResponse = todoService.getTodo(todoId);
        // then
        assertNotNull(todoResponse);
        assertEquals(1, todoResponse.getId());
    }


    @Test
    public void 일정_만들기() {
        // given
        long userId = 1L;
        AuthUser authUser = new AuthUser(userId, "email", UserRole.USER);
        TodoSaveRequest request = new TodoSaveRequest("title", "contents");
        User user = User.fromAuthUser(authUser);
        given(weatherClient.getTodayWeather()).willReturn("icy");

        Todo savedTodo = new Todo("title", "contents","icy", user);
        ReflectionTestUtils.setField(savedTodo,"id",1L);

        given(todoRepository.save(any(Todo.class))).willReturn(savedTodo);

        //when
        TodoSaveResponse todoSaveResponse = todoService.saveTodo(authUser, request);

        //then
        assertEquals(1L, todoSaveResponse.getId());
        assertNotNull(todoSaveResponse);
        assertEquals("title", todoSaveResponse.getTitle());
        assertEquals("contents", todoSaveResponse.getContents());
        assertEquals("icy", todoSaveResponse.getWeather());

    }

    @Test
    public void 일정조회_중_일정을_찾지못해_Todo_not_found출력() {
        //given
        long todoId = 1L;
        long userId = 2L;
        User user = new User("email", "12321", UserRole.USER);
        ReflectionTestUtils.setField(user, "id", userId);
        Todo todo = new Todo("title", "contents", "icy", user);
        ReflectionTestUtils.setField(todo, "id", todoId);
        given(todoRepository.findByIdWithUser(anyLong())).willReturn(Optional.empty());

        // when & then
        assertThrows(InvalidRequestException.class, ()-> todoService.getTodo(todoId),"Todo not found");
    }

    @Test
    public void 업데이트_성공() {
        // given
        User user = new User("email", "12321", UserRole.USER);
        Todo todo = new Todo("title", "contents", "weather", user);

        //when
        todo.update("updateTitle", "updateContents");

        //then
        assertEquals("updateTitle", todo.getTitle());
        assertEquals("updateContents", todo.getContents());
    }
}
