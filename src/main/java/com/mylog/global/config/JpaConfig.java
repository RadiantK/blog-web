package com.mylog.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

// root의 application클래스에 다는 것이 아닌 config를 분리하면 단위테스트 시 필요한 context만 불러올 때 유융
@EnableJpaAuditing
@Configuration
public class JpaConfig {
}
