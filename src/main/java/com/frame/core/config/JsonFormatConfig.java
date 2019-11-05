package com.frame.core.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.frame.core.utils.DateUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Configuration
public class JsonFormatConfig {

    @Bean
    public ObjectMapper objectMapper(){
        ObjectMapper mapper = new ObjectMapper();
        JavaTimeModule timeModule = new JavaTimeModule();
        timeModule.addSerializer(LocalDateTime.class,new LocalDateTimeToDateTimeSerializer());
        mapper.registerModule(timeModule);
        return mapper;
    }


    /** LocalDateTime 转 timestamp */
    public class LocalDateTimeToTimestampSerializer extends JsonSerializer<LocalDateTime> {
        @Override
        public void serialize(LocalDateTime localDateTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeNumber(localDateTime.toEpochSecond(ZoneOffset.ofHours(8)));
        }
    }
    /** LocalDateTime 转 datetime */
    public class LocalDateTimeToDateTimeSerializer extends JsonSerializer<LocalDateTime> {
        @Override
        public void serialize(LocalDateTime localDateTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeString(DateUtil.asDateTimeString(localDateTime));
        }
    }
    /** LocalDateTime 转 date */
    public class LocalDateTimeToDateSerializer extends JsonSerializer<LocalDateTime> {
        @Override
        public void serialize(LocalDateTime localDateTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeString(DateUtil.asDateString(localDateTime));
        }
    }
}
