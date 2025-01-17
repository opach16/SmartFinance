package com.konrad.smartfinance.mapper;

import com.konrad.smartfinance.domain.dto.UserDto;
import com.konrad.smartfinance.domain.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class UserMapperTestSuite {

    @Autowired
    private UserMapper userMapper;

    @Test
    void shouldMapToUserEntity() {
        //given
        UserDto userDto = new UserDto("testUsername", "testEmail", "testPassword");
        //when
        User mappedUser = userMapper.mapToUserEntity(userDto);
        //then
        assertNotNull(mappedUser);
        assertEquals(userDto.getUsername(), mappedUser.getUsername());
        assertEquals(userDto.getEmail(), mappedUser.getEmail());
        assertEquals(userDto.getPassword(), mappedUser.getPassword());
    }

    @Test
    void shouldMapToUserDto() {
        //when
        User user = User.builder()
                .id(1L)
                .username("testUsername")
                .email("testEmail")
                .password("testPassword")
                .createdAt(LocalDateTime.of(2020, 1, 1, 0, 0))
                .updatedAt(LocalDateTime.of(2020, 1, 1, 0, 0))
                .build();
        //when
        UserDto mappedUserDto = userMapper.mapToUserDto(user);
        //then
        assertNotNull(mappedUserDto);
        assertEquals(user.getId(), mappedUserDto.getId());
        assertEquals(user.getUsername(), mappedUserDto.getUsername());
        assertEquals(user.getEmail(), mappedUserDto.getEmail());
        assertEquals(user.getPassword(), mappedUserDto.getPassword());
        assertEquals(user.getCreatedAt(), mappedUserDto.getCreatedAt());
        assertEquals(user.getUpdatedAt(), mappedUserDto.getUpdatedAt());
    }

    @Test
    void shouldMapToUserDtoList() {
        //given
        User user1 = User.builder()
                .id(1L)
                .username("testUsername1")
                .email("testEmail1")
                .password("testPassword1")
                .createdAt(LocalDateTime.of(2020, 1, 1, 0, 0))
                .updatedAt(LocalDateTime.of(2020, 1, 1, 0, 0))
                .build();
        User user2 = User.builder()
                .id(2L)
                .username("testUsername2")
                .email("testEmail2")
                .password("testPassword2")
                .createdAt(LocalDateTime.of(2020, 1, 1, 0, 0))
                .updatedAt(LocalDateTime.of(2020, 1, 1, 0, 0))
                .build();
        List<User> userList = List.of(user1, user2);
        //when
        List<UserDto> mappedUserDtoList = userMapper.mapToUserDtoList(userList);
        //then
        assertNotNull(mappedUserDtoList);
        assertEquals(userList.size(), mappedUserDtoList.size());
        assertEquals(userList.getFirst().getId(), mappedUserDtoList.getFirst().getId());
        assertEquals(userList.getFirst().getUsername(), mappedUserDtoList.getFirst().getUsername());
        assertEquals(userList.getLast().getId(), mappedUserDtoList.getLast().getId());
    }
}