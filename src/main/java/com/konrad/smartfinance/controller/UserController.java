package com.konrad.smartfinance.controller;

import com.konrad.smartfinance.domain.dto.UserDto;
import com.konrad.smartfinance.domain.model.User;
import com.konrad.smartfinance.exception.UserException;
import com.konrad.smartfinance.mapper.UserMapper;
import com.konrad.smartfinance.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userMapper.mapToUserDtoList(userService.getAllUsers()));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) throws UserException {
        return ResponseEntity.ok(userMapper.mapToUserDto(userService.getUserById(id)));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        User addedUser = userService.addUser(userMapper.mapToUserEntity(userDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.mapToUserDto(addedUser));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UserDto userDto) throws UserException {
        User updatedUser = userService.updateUser(id, userMapper.mapToUserEntity(userDto));
        return ResponseEntity.ok(userMapper.mapToUserDto(updatedUser));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) throws UserException {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
}
