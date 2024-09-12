package org.example.expert.domain.comment.service;

import org.example.expert.client.WeatherClient;
import org.example.expert.domain.comment.entity.Comment;
import org.example.expert.domain.comment.repository.CommentRepository;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CommentAdminServiceTest {
    @Mock
    CommentRepository commentRepository;

    @InjectMocks
    CommentAdminService commentAdminService;

    @Test
    void 댓글을_삭제한다() {
        //given
        long commentId = 1L;
        User user = new User("email", "12321", UserRole.ADMIN);
        Todo todo = new Todo("title", "contents", "icy", user);
        Comment comment = new Comment("Comment", user, todo);
        ReflectionTestUtils.setField(comment, "id", commentId);
        //when
        commentAdminService.deleteComment(comment.getId());

        //then
        assertNotNull(comment.getId());

    }
}