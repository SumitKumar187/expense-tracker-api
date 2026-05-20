package expense_tracker.service;

import expense_tracker.dto.request.LoginRequest;
import expense_tracker.dto.request.RegisterRequest;
import expense_tracker.dto.response.AuthResponse;
import expense_tracker.entity.User;
import expense_tracker.repository.UserRepository;
import expense_tracker.security.JwtService;
import expense_tracker.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthServiceImpl authService;

    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private User user;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest();
        registerRequest.setUsername("testuser");
        registerRequest.setEmail("test@test.com");
        registerRequest.setPassword("password123");

        loginRequest = new LoginRequest();
        loginRequest.setEmail("test@test.com");
        loginRequest.setPassword("password123");

        user = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@test.com")
                .password("encodedPassword")
                .build();
    }

    @Test
    void register_Success() {
        // Given
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtService.generateToken(any(User.class))).thenReturn("jwt-token");

        // When
        AuthResponse response = authService.register(registerRequest);

        // Then
        assertNotNull(response);
        assertEquals("jwt-token", response.getToken());
        assertEquals("testuser", response.getUsername());
        assertEquals("test@test.com", response.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void register_EmailAlreadyExists_ThrowsException() {
        // Given
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        // When & Then
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> authService.register(registerRequest)
        );
        assertEquals("Email already registered", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void register_UsernameAlreadyExists_ThrowsException() {
        // Given
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByUsername(anyString())).thenReturn(true);

        // When & Then
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> authService.register(registerRequest)
        );
        assertEquals("Username already taken", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void login_Success() {
        // Given
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(any(User.class))).thenReturn("jwt-token");

        // When
        AuthResponse response = authService.login(loginRequest);

        // Then
        assertNotNull(response);
        assertEquals("jwt-token", response.getToken());
        verify(authenticationManager, times(1)).authenticate(any());
    }

    @Test
    void login_InvalidCredentials_ThrowsException() {
        // Given
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        // When & Then
        assertThrows(BadCredentialsException.class, () -> authService.login(loginRequest));
    }
}