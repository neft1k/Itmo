package com.example.demo;

import com.example.demo.model.UserEntity;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserEntityTest {

    @Test
    void testSettersAndGetters() {
        UserEntity user = new UserEntity();
        user.setUsername("karim");
        user.setPasswordHash("karim");

        assertThat(user.getUsername()).isEqualTo("karim");
        assertThat(user.getPasswordHash()).isEqualTo("karim");
    }

    @Test
    void testConstructor() {
        UserEntity user = new UserEntity("karim2", "karim2");
        assertThat(user.getUsername()).isEqualTo("karim2");
        assertThat(user.getPasswordHash()).isEqualTo("karim2");
    }
}
