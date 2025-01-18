package com.konrad.smartfinance.controller;

import com.konrad.smartfinance.domain.dto.LoginData;
import com.konrad.smartfinance.domain.dto.UserDetailsDto;
import com.konrad.smartfinance.exception.UserException;
import com.konrad.smartfinance.repository.UserRepository;
import com.konrad.smartfinance.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Scope("session")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final HttpSession httpSession;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<UserDetailsDto> login(@RequestBody LoginData loginData) throws UserException {
        Authentication authenticationRequest = UsernamePasswordAuthenticationToken.unauthenticated(loginData.getUsername(), loginData.getPassword());
        Authentication authenticationResponse = authenticationManager.authenticate(authenticationRequest);
        String username = authenticationResponse.getName();
        Long userId = userService.getUserByUsername(authenticationResponse.getName()).getId();
        UserDetailsDto userDetailsDto = new UserDetailsDto(username, userId);
        httpSession.setAttribute(username, authenticationResponse);
        return ResponseEntity.ok(userDetailsDto);
    }
}
