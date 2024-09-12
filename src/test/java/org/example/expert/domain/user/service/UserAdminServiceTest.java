package org.example.expert.domain.user.service;

import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.user.dto.request.UserRoleChangeRequest;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserAdminServiceTest {
    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserAdminService userAdminService;

    @Test
    void 유저권한_변경_성공() {
        //given
        long userId = 1L;
        UserRoleChangeRequest userRoleChangeRequest = new UserRoleChangeRequest("USER");
        User findUser = new User("email", "12321", UserRole.USER);
        ReflectionTestUtils.setField(findUser, "id", userId);
        ReflectionTestUtils.setField(userRoleChangeRequest, "role", "ADMIN");
        given(userRepository.findById(anyLong())).willReturn(Optional.of(findUser));

        //when
        userAdminService.changeUserRole(userId, userRoleChangeRequest);
        assertEquals(UserRole.ADMIN, findUser.getUserRole());
    }

    @Test
    void 유저권한_변경시_예외출력() {
        //given
        long userId = 1L;
        UserRoleChangeRequest userRoleChangeRequest = new UserRoleChangeRequest("ADMIN");
        given(userRepository.findById(anyLong())).willReturn(Optional.empty());

        //when & Then
        assertThrows(InvalidRequestException.class, ()-> {userAdminService.changeUserRole(userId, userRoleChangeRequest);});
    }
}