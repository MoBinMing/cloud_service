package com.mobinming.cloud_service.config;


import com.google.gson.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author leizige
 */
@Configuration
public class GSONConfig {

    //序列化
//    final static JsonSerializer<LocalDateTime> jsonSerializerDateTime = (localDateTime, type, jsonSerializationContext) -> new JsonPrimitive(localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
//    final static JsonSerializer<LocalDate> jsonSerializerDate = (localDate, type, jsonSerializationContext) -> new JsonPrimitive(localDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
//    //反序列化
//    final static JsonDeserializer<LocalDateTime> jsonDeserializerDateTime = (jsonElement, type, jsonDeserializationContext) -> LocalDateTime.parse(jsonElement.getAsJsonPrimitive().getAsString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
//    final static JsonDeserializer<LocalDate> jsonDeserializerDate = (jsonElement, type, jsonDeserializationContext) -> LocalDate.parse(jsonElement.getAsJsonPrimitive().getAsString(), DateTimeFormatter.ISO_LOCAL_DATE);

//    @Bean
//    public static Gson create() {
//        return new GsonBuilder()
//                .setPrettyPrinting()
//                /* 更改先后顺序没有影响 */
//                .registerTypeAdapter(LocalDateTime.class, jsonSerializerDateTime)
//                .registerTypeAdapter(LocalDate.class, jsonSerializerDate)
//                .registerTypeAdapter(LocalDateTime.class, jsonDeserializerDateTime)
//                .registerTypeAdapter(LocalDate.class, jsonDeserializerDate)
//                .create();
//    }
    @Bean
    public static Gson create() {
        return new GsonBuilder().create();
    }
}