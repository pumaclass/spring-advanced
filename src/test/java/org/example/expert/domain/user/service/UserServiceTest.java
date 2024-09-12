package org.example.expert.domain.user.service;

import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.user.dto.request.UserChangePasswordRequest;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    @Test
    public void 회원조회_성공() {
        //given
        long userId = 1L;
        User user = new User("email", "12321", UserRole.USER);
        ReflectionTestUtils.setField(user, "id", userId);

        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));

        //when
        UserResponse findUser = userService.getUser(userId);

        //then
        assertNotNull(findUser);
        assertEquals("email", user.getEmail());
    }

    @Test
    public void 회원조회시_User_not_found에러_출력() {
        //given
        long userId = 1L;
        User user = new User("email", "12321", UserRole.USER);
        ReflectionTestUtils.setField(user, "id", userId);

        given(userRepository.findById(anyLong())).willReturn(Optional.empty());

        //when&then
        assertThrows(InvalidRequestException.class, ()-> userService.getUser(userId),"User not found");
    }

    @Test
    public void 비밀번호_변경시_새_비밀번호에_숫자가_없으면_예외발생() {
        // given
        long userId = 1L;
        UserChangePasswordRequest request = new UserChangePasswordRequest("oldPassword", "NoNumberPassword");

        // when & then
        assertThrows(InvalidRequestException.class,
                () -> userService.validateUserPassword(userId, request),
                "새 비밀번호는 8자 이상이어야 하고, 숫자와 대문자를 포함해야 합니다.");
    }

    @Test
    public void 비밀번호_변경시_비밀번호가_짧으면_예외발생() {
        // given
        long userId = 1L;
        UserChangePasswordRequest request = new UserChangePasswordRequest("oldPassword", "short");

        // when & then
        assertThrows(InvalidRequestException.class,
                () -> userService.validateUserPassword(userId, request),
                "새 비밀번호는 8자 이상이어야 하고, 숫자와 대문자를 포함해야 합니다.");
    }

    @Test
    public void 비밀번호_변경시_대문자가_없으면_예외발생() {
        // given
        long userId = 1L;
        UserChangePasswordRequest request = new UserChangePasswordRequest("oldPassword", "giles12321");

        // when & then
        assertThrows(InvalidRequestException.class,
                () -> userService.validateUserPassword(userId, request),
                "새 비밀번호는 8자 이상이어야 하고, 숫자와 대문자를 포함해야 합니다.");
    }
}