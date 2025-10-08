package com.example.demo.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
public class LogDsOnStart {

    private static final Logger log = LoggerFactory.getLogger(LogDsOnStart.class);
    private final DataSource ds;

    public LogDsOnStart(DataSource ds) {
        this.ds = ds;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onReady() {
        if (ds instanceof HikariDataSource hds) {
            log.info("DS URL = {}", hds.getJdbcUrl());
            log.info("DS USER = {}", hds.getUsername());
        } else {
            log.info("DataSource = {}", ds);
        }
    }
}
