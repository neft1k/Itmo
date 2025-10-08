package com.example.demo;

import com.example.demo.model.UserEntity;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;


class UserServiceTest {

    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    void beforeAllTests() {
        userRepository = mock(UserRepository.class);
        userService   = new UserService(userRepository);
    }


    @Test
    void okPassword() {
        String raw = "112233";
        String hash = new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder().encode(raw);
        when(userRepository.findByUsername("karim")).thenReturn(
                Optional.of(new UserEntity("karim", hash))
        );

        assertThat(userService.checkCredentials("karim", raw)).isTrue();
    }

    @Test
    void noUser() {
        when(userRepository.findByUsername("karim2")).thenReturn(Optional.empty());

        assertThat(userService.checkCredentials("karim2", "123456")).isFalse();
    }

    @Test
    void wrongPassword() {
        String otherHash = new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder()
                .encode("123");

        when(userRepository.findByUsername("karim"))
                .thenReturn(Optional.of(new UserEntity("karim", otherHash)));

        assertThat(userService.checkCredentials("karim", "112233")).isFalse();
    }
    @Test
    void saveUser() {
        when(userRepository.findByUsername("karim3")).thenReturn(Optional.empty());

        userService.register("karim3", "1234");

        ArgumentCaptor<UserEntity> captor = ArgumentCaptor.forClass(UserEntity.class);
        verify(userRepository).save(captor.capture());
        UserEntity saved = captor.getValue();

        assertThat(saved.getUsername()).isEqualTo("karim3");
        assertThat(saved.getPasswordHash()).isNotEqualTo("1234");
    }

    @Test
    void userExists() {
        when(userRepository.findByUsername("karim"))
                .thenReturn(Optional.of(new UserEntity("karim", "karim")));

        assertThatThrownBy(() -> userService.register("karim", "1234"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("User already exists");
    }


}

