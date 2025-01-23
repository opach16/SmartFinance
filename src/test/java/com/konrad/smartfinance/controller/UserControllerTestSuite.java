package com.konrad.smartfinance.controller;

import com.google.gson.*;
import com.konrad.smartfinance.domain.dto.CreateUserRequestDto;
import com.konrad.smartfinance.domain.dto.UserDto;
import com.konrad.smartfinance.domain.model.User;
import com.konrad.smartfinance.exception.UserException;
import com.konrad.smartfinance.mapper.UserMapper;
import com.konrad.smartfinance.service.UserService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@SpringJUnitWebConfig
@WebMvcTest(UserController.class)
class UserControllerTestSuite {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private UserMapper userMapper;

    private final GsonBuilder gsonBuilder =
            new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer());

    private final Gson gson = gsonBuilder.create();

    @Test
    @WithMockUser
    void shouldGetEmptyList() throws Exception {
        //given
        when(userService.getAllUsers()).thenReturn(Collections.emptyList());
        //when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(0)));
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    @WithMockUser
    void shouldGetAllUsers() throws Exception {
        //given
        UserDto user1 = UserDto.builder()
                .id(1L)
                .username("testUsername1")
                .email("testEmail1")
                .password("testPassword1")
                .build();
        UserDto user2 = UserDto.builder()
                .id(2L)
                .username("testUsername2")
                .email("testEmail2")
                .password("testPassword2")
                .build();
        when(userService.getAllUsers()).thenReturn(Collections.emptyList());
        when(userMapper.mapToUserDtoList(anyList())).thenReturn(List.of(user1, user2));
        //when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].username", Matchers.is("testUsername1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id", Matchers.is(2)));
        verify(userService, times(1)).getAllUsers();
        verify(userMapper, times(1)).mapToUserDtoList(anyList());
    }

    @Test
    @WithMockUser
    void shouldGetUserById() throws Exception {
        //given
        Long userId = 1L;
        UserDto user1 = UserDto.builder()
                .id(1L)
                .username("testUsername1")
                .email("testEmail1")
                .password("testPassword1")
                .build();
        when(userService.getUserById(userId)).thenReturn(null);
        when(userMapper.mapToUserDto(any())).thenReturn(user1);
        //when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username", Matchers.is("testUsername1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", Matchers.is("testEmail1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password", Matchers.is("testPassword1")));
        verify(userService, times(1)).getUserById(userId);
        verify(userMapper, times(1)).mapToUserDto(any());
    }

    @Test
    @WithMockUser
    void shouldReturnBadRequestWhenUserNotFoundOnGetUser() throws Exception {
        //given
        Long userId = 1L;
        when(userService.getUserById(userId)).thenThrow(new UserException(UserException.USER_NOT_FOUND));
        //when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString(UserException.USER_NOT_FOUND)));
        verify(userService, times(1)).getUserById(userId);
    }

    @Test
    @WithMockUser
    void shouldCreateUser() throws Exception {
        //given
        UserDto user = UserDto.builder()
                .id(1L)
                .username("testUsername")
                .email("testEmail")
                .password("testPassword")
                .build();
        CreateUserRequestDto request = new CreateUserRequestDto(user, "USD", BigDecimal.ONE);
        when(userService.addUser(any(), any(), any())).thenReturn(null);
        when(userMapper.mapToUserDto(any())).thenReturn(user);
        String userRequestJson = gson.toJson(request);
        //when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/users").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userRequestJson))
                .andExpect(MockMvcResultMatchers.status().isCreated());
        verify(userService, times(1)).addUser(any(), any(), any());
        verify(userMapper, times(1)).mapToUserDto(any());
    }

    @Test
    @WithMockUser
    void shouldReturnBadRequestWhenUserAlreadyExistsOnCreateUser() throws Exception {
        //given
        UserDto userDto = UserDto.builder()
                .id(1L)
                .username("testUsername")
                .email("testEmail")
                .password("testPassword")
                .build();
        User user = User.builder()
                .id(1L)
                .username("testUsername")
                .email("testEmail")
                .password("testPassword")
                .build();
        CreateUserRequestDto request = new CreateUserRequestDto(userDto, "USD", BigDecimal.ONE);
        when(userMapper.mapToUserEntity(userDto)).thenReturn(user);
        doThrow(new UserException("Username: " + user.getUsername() + " already exists")).when(userService).addUser(user, "USD", BigDecimal.ONE);
        String userRequestJson = gson.toJson(request);
        //when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/users").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userRequestJson))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("Username: " + user.getUsername() + " already exists")));
        verify(userService, times(1)).addUser(user, "USD", BigDecimal.ONE);
        verify(userMapper, times(1)).mapToUserEntity(userDto);
    }

    @Test
    @WithMockUser
    void shouldUpdateUser() throws Exception {
        //given
        Long userId = 1L;
        UserDto userDto = UserDto.builder()
                .id(1L)
                .username("testUsernameUpdated")
                .email("testEmailUpdated")
                .password("testPasswordUpdated")
                .build();
        User user = User.builder()
                .id(1L)
                .username("testUsernameUpdated")
                .email("testEmailUpdated")
                .password("testPasswordUpdated")
                .build();
        String userRequestJson = gson.toJson(userDto);
        when(userMapper.mapToUserEntity(userDto)).thenReturn(user);
        when(userMapper.mapToUserDto(user)).thenReturn(userDto);
        when(userService.updateUser(userId, user)).thenReturn(user);
        //when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/users/{id}", userId).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userRequestJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username", Matchers.is("testUsernameUpdated")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", Matchers.is("testEmailUpdated")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password", Matchers.is("testPasswordUpdated")));
        verify(userService, times(1)).updateUser(userId, user);
        verify(userMapper, times(1)).mapToUserEntity(userDto);
    }

    @Test
    @WithMockUser
    void shouldReturnBadRequestWhenUserNotFoundOnUpdateUser() throws Exception {
        //given
        Long userId = 1L;
        UserDto userDto = UserDto.builder()
                .id(1L)
                .username("testUsernameUpdated")
                .email("testEmailUpdated")
                .password("testPasswordUpdated")
                .build();
        User user = User.builder()
                .id(1L)
                .username("testUsernameUpdated")
                .email("testEmailUpdated")
                .password("testPasswordUpdated")
                .build();
        String userRequestJson = gson.toJson(userDto);
        doThrow(new UserException(UserException.USER_NOT_FOUND)).when(userService).updateUser(userId, user);
        when(userMapper.mapToUserEntity(userDto)).thenReturn(user);
        //when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/users/{id}", userId).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userRequestJson))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString(UserException.USER_NOT_FOUND)));
        verify(userService, times(1)).updateUser(userId, user);
        verify(userMapper, times(1)).mapToUserEntity(userDto);
    }

    @Test
    @WithMockUser
    void shouldReturnBadRequestWhenUserNotFoundOnDeleteUser() throws Exception {
        //given
        Long userId = 1L;
        doThrow(new UserException(UserException.USER_NOT_FOUND)).when(userService).deleteUser(userId);
        //when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/users/{id}", userId).with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString(UserException.USER_NOT_FOUND)));
        verify(userService, times(1)).deleteUser(userId);
    }

    private static final class LocalDateTimeSerializer implements JsonSerializer<LocalDateTime> {
        private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss");

        @Override
        public JsonElement serialize(LocalDateTime localDateTime, Type srcType, JsonSerializationContext context) {
            return new JsonPrimitive(formatter.format(localDateTime));
        }
    }
}

