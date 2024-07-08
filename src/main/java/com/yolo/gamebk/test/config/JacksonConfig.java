package com.yolo.gamebk.test.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 7/5/24
 * Time: 9:57 PM
 *   This class provides configuration for Jackson, a popular JSON processing library.
 */

@Configuration
public class JacksonConfig {


    /**
     * Defines a bean for ObjectMapper which is used for JSON serialization and deserialization.
     * @return a configured ObjectMapper instance
     */
    @Bean
    public ObjectMapper objectMapper(){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        // Disable writing dates as timestamps
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return objectMapper;
    }
}
