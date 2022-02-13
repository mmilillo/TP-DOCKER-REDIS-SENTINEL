package ar.edu.undav.noescalapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration
@EnableRedisRepositories
public class RedisConfig {

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        //return new LettuceConnectionFactory(new RedisStandaloneConfiguration("redisDB-master", 6379));
        RedisSentinelConfiguration sentinelConfig = new RedisSentinelConfiguration().master("my_redis_master")
        .sentinel("172.20.0.6",26379)
        .sentinel("172.20.0.7",26380)
        .sentinel("172.20.0.8",26381);
        return new LettuceConnectionFactory(sentinelConfig);
    }


}
