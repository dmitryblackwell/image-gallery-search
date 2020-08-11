package com.blackwell;

import com.blackwell.response.FullPictureResponse;
import com.blackwell.response.PicturesResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootApplication
public class ImageGallerySearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImageGallerySearchApplication.class, args);
    }

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private String redisPort;

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(redisHost, Integer.parseInt(redisPort));
        return new JedisConnectionFactory(redisStandaloneConfiguration);
    }

    @Bean
    RedisTemplate<String, PicturesResponse> redisPicturesTemplate() {
        RedisTemplate<String, PicturesResponse> redisPicturesTemplate = new RedisTemplate<>();
        redisPicturesTemplate.setConnectionFactory(jedisConnectionFactory());
        return redisPicturesTemplate;
    }

    @Bean
    RedisTemplate<String, FullPictureResponse> redisPictureTemplate() {
        RedisTemplate<String, FullPictureResponse> redisPictureTemplate = new RedisTemplate<>();
        redisPictureTemplate.setConnectionFactory(jedisConnectionFactory());
        return redisPictureTemplate;
    }
}
