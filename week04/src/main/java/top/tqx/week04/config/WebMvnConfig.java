package top.tqx.week04.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class WebMvnConfig implements WebMvcConfigurer {

    @Bean
    public MappingJackson2HttpMessageConverter customJacksonConverter() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json()
                .modules(new JavaTimeModule().addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(formatter)),
                        new SimpleModule().addSerializer(Long.class, ToStringSerializer.instance))
                .build();
        return new MappingJackson2HttpMessageConverter(objectMapper);
    }


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/upload/**").addResourceLocations("classpath:/static/upload/");
        String userDir = System.getProperty("user.dir");
        String uploadPath = "file:" + userDir + File.separator + "static" + File.separator + "upload" + File.separator;

        registry.addResourceHandler("/upload/**")
                .addResourceLocations(uploadPath);

        System.out.println("静态资源映射：/upload/** -> " + uploadPath);
    }


}
