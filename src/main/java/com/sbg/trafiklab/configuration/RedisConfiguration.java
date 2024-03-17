package com.sbg.trafiklab.configuration;

import com.sbg.trafiklab.entity.Line;
import com.sbg.trafiklab.entity.Stop;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfiguration {

    public <T> ReactiveRedisOperations<String, T> reactiveRedisOperations(ReactiveRedisConnectionFactory factory,
            Class<T> clazz) {
        Jackson2JsonRedisSerializer<T> serializer = new Jackson2JsonRedisSerializer<>(clazz);

        RedisSerializationContext.RedisSerializationContextBuilder<String, T> builder =
                RedisSerializationContext.newSerializationContext(new StringRedisSerializer());

        RedisSerializationContext<String, T> context = builder.value(serializer).build();

        return new ReactiveRedisTemplate<>(factory, context);
    }

    @Bean
    public ReactiveRedisOperations<String, Stop> stopOperations(ReactiveRedisConnectionFactory factory) {
        return reactiveRedisOperations(factory, Stop.class);
    }

    @Bean
    public ReactiveRedisOperations<String, Line> lineOperations(ReactiveRedisConnectionFactory factory) {
        return reactiveRedisOperations(factory, Line.class);
    }

    @Bean
    public ReactiveRedisOperations<String, Integer> integerOperations(ReactiveRedisConnectionFactory factory) {
        return reactiveRedisOperations(factory, Integer.class);
    }

}
