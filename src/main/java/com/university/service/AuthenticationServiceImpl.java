package com.university.service;


import com.university.dto.AuthenticationRequest;
import com.university.dto.AuthenticationResponse;
import com.university.dto.RegisterRequest;
import com.university.exception.ResourceNotFoundException;
import com.university.model.Role;
import com.university.model.User;
import com.university.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtServiceImpl jwtServiceImpl;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest registerRequest) {
        var user = User.builder()
                .firstName(registerRequest.firstName())
                .lastName(registerRequest.lastName())
                .email(registerRequest.email())
                .password(passwordEncoder.encode(registerRequest.password()))
                .role(Role.USER)
                .build();

        userRepository.save(user);
        var jwtToken = jwtServiceImpl.generateToken(user);
        return new AuthenticationResponse(jwtToken, registerRequest.firstName());
    }

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.email(),
                        authenticationRequest.password()
                )
        );
        var user = userRepository.findByEmail(authenticationRequest.email())
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));
        var jwtToken = jwtServiceImpl.generateToken(user);
        return new AuthenticationResponse(jwtToken, user.getFirstName());
    }
}
