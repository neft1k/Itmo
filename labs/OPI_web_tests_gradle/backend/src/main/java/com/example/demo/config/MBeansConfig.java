package com.example.demo.config;

import com.example.demo.metrics.ClickInterval;
import com.example.demo.metrics.PointsStats;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

@Configuration
public class MBeansConfig {

    @Bean
    public PointsStats pointsStats() { return new PointsStats(); }

    @Bean
    public ClickInterval clickInterval() { return new ClickInterval(); }

    @Bean
    public CommandLineRunner mbeanRegistrar(PointsStats points, ClickInterval clicks) {
        return args -> {
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
            mbs.registerMBean(points, new ObjectName("com.example.demo:type=PointsStats"));
            mbs.registerMBean(clicks,  new ObjectName("com.example.demo:type=ClickInterval"));
        };
    }
}

