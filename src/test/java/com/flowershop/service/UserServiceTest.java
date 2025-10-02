package com.flowershop.service;

import com.flowershop.model.User;
import com.flowershop.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserService userService;

    @Test
    void testRegisterUser_Success() {

        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("password123");
        user.setFullName("Test User");

        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            savedUser.setId(1L);
            return savedUser;
        });
        User result = userService.registerUser(user);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("encodedPassword", result.getPassword());
        assertNotNull(result.getCreatedAt());

        verify(userRepository).existsByUsername("testuser");
        verify(userRepository).existsByEmail("test@example.com");
        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(user);
    }
    @Test
    void testAuthenticateUser_Success() {
        String username = "testuser";
        String password = "password123";

        User user = new User();
        user.setId(1L);
        user.setUsername(username);
        user.setPassword("encodedPassword");
        user.setEmail("test@example.com");
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, "encodedPassword")).thenReturn(true);
        User result = userService.authenticateUser(username, password);
        assertNotNull(result);
        assertEquals(username, result.getUsername());
        verify(userRepository).findByUsername(username);
        verify(passwordEncoder).matches(password, "encodedPassword");}
    @Test
    void testAuthenticateUser_UserNotFound() {
        String username = "nonexistent";
        String password = "password123";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.authenticateUser(username, password);
        });
        assertEquals("User not found", exception.getMessage());
        verify(userRepository).findByUsername(username);
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }
    @Test
    void testAuthenticateUser_InvalidPassword() {
        String username = "testuser";
        String password = "wrongpassword";

        User user = new User();
        user.setId(1L);
        user.setUsername(username);
        user.setPassword("encodedPassword");

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, "encodedPassword")).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.authenticateUser(username, password);
        });
        assertEquals("Invalid password", exception.getMessage());
        verify(userRepository).findByUsername(username);
        verify(passwordEncoder).matches(password, "encodedPassword");
    }
}