package winnguyen1905.cart.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.*;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.*;

import io.lettuce.core.ClientOptions;
import io.lettuce.core.SocketOptions;
import io.lettuce.core.TimeoutOptions;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;

/**
 * Configuration class for Redis connection and template setup in Cart service.
 * Supports reading regional cache data from gateway service.
 */
@Slf4j
@Configuration
public class RedisConfig {

  // Redis connection properties with default values
  private static final String DEFAULT_REDIS_HOST = "localhost";
  private static final int DEFAULT_REDIS_PORT = 6379;
  private static final long DEFAULT_TIMEOUT_MS = 2000L;
  private static final String DEFAULT_PASSWORD = "mypassword";

  @Value("${spring.data.redis.host:" + DEFAULT_REDIS_HOST + "}")
  private String redisHost;

  @Value("${spring.data.redis.port:" + DEFAULT_REDIS_PORT + "}")
  private int redisPort;

  @Value("${spring.data.redis.timeout:" + DEFAULT_TIMEOUT_MS + "}")
  private long redisTimeout;

  @Value("${spring.data.redis.password:" + DEFAULT_PASSWORD + "}")
  private String redisPassword;

  /**
   * Creates a Redis connection factory.
   */
  @Bean
  @Primary
  public RedisConnectionFactory redisConnectionFactory() {
    log.info("Creating RedisConnectionFactory for Cart service at {}:{}", redisHost, redisPort);
    return new LettuceConnectionFactory(redisStandaloneConfiguration(), lettuceClientConfiguration());
  }

  /**
   * Configures Redis standalone connection details.
   */
  private RedisStandaloneConfiguration redisStandaloneConfiguration() {
    RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
    config.setHostName(redisHost);
    config.setPort(redisPort);
    if (redisPassword != null && !redisPassword.isEmpty()) {
      config.setPassword(RedisPassword.of(redisPassword));
    }
    return config;
  }

  /**
   * Configures Lettuce client settings.
   */
  private LettuceClientConfiguration lettuceClientConfiguration() {
    return LettuceClientConfiguration.builder()
        .commandTimeout(Duration.ofMillis(redisTimeout))
        .clientOptions(ClientOptions.builder()
            .autoReconnect(true)
            .disconnectedBehavior(ClientOptions.DisconnectedBehavior.REJECT_COMMANDS)
            .socketOptions(SocketOptions.builder()
                .connectTimeout(Duration.ofMillis(redisTimeout))
                .keepAlive(true)
                .build())
            .timeoutOptions(TimeoutOptions.enabled(Duration.ofMillis(redisTimeout)))
            .build())
        .build();
  }

  /**
   * Creates a StringRedisTemplate for simple String operations.
   * Used primarily for reading IP-region cache from gateway service.
   */
  @Bean
  public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory factory) {
    log.debug("Creating StringRedisTemplate for Cart service");
    
    StringRedisTemplate template = new StringRedisTemplate();
    template.setConnectionFactory(factory);
    template.afterPropertiesSet();
    return template;
  }

  /**
   * Creates a Redis template for Object serialization.
   */
  @Bean
  public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
    log.debug("Creating RedisTemplate for Cart service");

    RedisTemplate<String, Object> template = new RedisTemplate<>();
    template.setConnectionFactory(factory);

    // Configure serializers
    Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
    template.setKeySerializer(new StringRedisSerializer());
    template.setValueSerializer(serializer);
    template.setHashKeySerializer(new StringRedisSerializer());
    template.setHashValueSerializer(serializer);

    template.afterPropertiesSet();
    return template;
  }
} 
