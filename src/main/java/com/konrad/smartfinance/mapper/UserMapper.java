package com.konrad.smartfinance.mapper;

import com.konrad.smartfinance.domain.dto.UserDto;
import com.konrad.smartfinance.domain.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserMapper {

    public User mapToUserEntity(UserDto userDto) {
        return User.builder()
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .build();
    }

    public UserDto mapToUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    public List<User> mapToUserEntityList(List<UserDto> userDtoList) {
        return userDtoList.stream()
                .map(this::mapToUserEntity)
                .toList();
    }

    public List<UserDto> mapToUserDtoList(List<User> userList) {
        return userList.stream()
                .map(this::mapToUserDto)
                .toList();
    }
}
