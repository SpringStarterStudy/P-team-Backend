package com.demo.pteam.global.config;

import ch.qos.logback.classic.spi.ConfiguratorRank;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@ConfiguratorRank
@EnableJpaAuditing
public class JpaConfig {
}
