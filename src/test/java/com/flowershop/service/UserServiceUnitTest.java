package com.flowershop.service;

import com.flowershop.model.User;
import com.flowershop.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;



import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceUnitTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserService userService;

    // Test 3: Registration with null email
    @Test
    void testRegisterUser_NullEmail() {
        User user = new User();
        user.setUsername("testuser");
        user.setEmail(null);
        user.setPassword("password123");
        user.setFullName("Test User");

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.registerUser(user);
        });
        assertEquals("Email is required", exception.getMessage());
        verify(userRepository, never()).existsByUsername(anyString());
        verify(userRepository, never()).existsByEmail(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    // Test 4: Registration with existing email
    @Test
    void testRegisterUser_EmailAlreadyExists() {
        User user = new User();
        user.setUsername("newuser");
        user.setEmail("existing@example.com");
        user.setPassword("password123");
        user.setFullName("Test User");

        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(userRepository.existsByEmail("existing@example.com")).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.registerUser(user);
        });
        assertEquals("Email already exists", exception.getMessage());
        verify(userRepository).existsByUsername("newuser");
        verify(userRepository).existsByEmail("existing@example.com");
        verify(userRepository, never()).save(any(User.class));
    }
}