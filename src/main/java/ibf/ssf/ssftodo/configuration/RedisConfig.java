package ibf.ssf.ssftodo.configuration;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import ibf.ssf.ssftodo.Constants;
import ibf.ssf.ssftodo.SsftodoApplication;


@Configuration
public class RedisConfig {
    private final Logger logger = Logger.getLogger(SsftodoApplication.class.getName());
        @Value("${spring.redis.host}") 
        private String redisHost;

        @Value("${spring.redis.port}") 
        private Optional<Integer> redisPort;

        @Value("${spring.redis.database}")
        private int redisDatabase; 
        
        @Bean(Constants.TODO_REDIS) // specify bean name in Bean() to do injection by name
        @Scope("singleton")
        public RedisTemplate<String, String> redisTemplate(){
                final RedisStandaloneConfiguration config 
                        = new RedisStandaloneConfiguration();
                logger.log(Level.INFO, "redis host: " + redisHost + " redis port: " + redisPort);
                // logger.info("redis host port> " + 
                //         redisHost + ' ' + redisPort.get() + ' ' + redisPassword);
                 
                config.setHostName(redisHost);
                config.setPort(redisPort.get());
                config.setDatabase(redisDatabase);

                final String redisPassword = System.getenv(Constants.REDIS_PASSWORD);
                if (null != redisPassword) {
                    logger.log(Level.INFO, "Setting Redis Password");
                    config.setPassword(redisPassword);
                }

                final JedisClientConfiguration jedisClient = JedisClientConfiguration
                                .builder().build();
                final JedisConnectionFactory jedisFac = 
                        new JedisConnectionFactory(config, jedisClient); 
                jedisFac.afterPropertiesSet();
                        
                RedisTemplate<String, String> template = new RedisTemplate<>();
                template.setConnectionFactory(jedisFac);
                template.setKeySerializer(new StringRedisSerializer()); 
                template.setValueSerializer(new StringRedisSerializer()); 

                return template;
        }
}
