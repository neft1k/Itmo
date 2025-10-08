package com.example.demo;

import com.example.demo.model.PointEntity;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PointEntityTest {

    @Test
    void testConstructor() {
        PointEntity point = new PointEntity(1.0, 2.0, 3.0, true);

        assertThat(point.getX()).isEqualTo(1.0);
        assertThat(point.getY()).isEqualTo(2.0);
        assertThat(point.getR()).isEqualTo(3.0);
        assertThat(point.isHit()).isTrue();
    }

    @Test
    void testSetters() {
        PointEntity point = new PointEntity();
        point.setX(4.5);
        point.setY(5.5);
        point.setR(6.5);
        point.setHit(false);

        assertThat(point.getX()).isEqualTo(4.5);
        assertThat(point.getY()).isEqualTo(5.5);
        assertThat(point.getR()).isEqualTo(6.5);
        assertThat(point.isHit()).isFalse();
    }
}
