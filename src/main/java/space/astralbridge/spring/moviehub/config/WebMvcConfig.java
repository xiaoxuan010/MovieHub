package space.astralbridge.spring.moviehub.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Bean
    public ObjectMapper objectMapper() {
        return Jackson2ObjectMapperBuilder.json()
                .featuresToEnable(MapperFeature.DEFAULT_VIEW_INCLUSION) // 启用默认视图包含
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS) // 禁用时间戳数组
                .modules(new JavaTimeModule()) // 注册 Java 时间模块
                .build();
    }
}
