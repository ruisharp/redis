package com.test.config;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;

import javax.annotation.Resource;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
@Configuration
@ConditionalOnClass({RedisClusterConfig.class})
@EnableConfigurationProperties({RedisClusterProperties.class})
public class RedisClusterConfig {
	@Resource
	private RedisClusterProperties redisClusterProperties;

	@Bean
	public JedisCluster redisCluster() {
		HashSet nodes = new HashSet();
		Iterator poolConfig = this.redisClusterProperties.getNodes().iterator();

		while (poolConfig.hasNext()) {
			String node = (String) poolConfig.next();
			String[] parts = StringUtils.split(node, ":");
			Assert.state(parts.length == 2,
					"redis node shoule be defined as \'host:port\', not \'" + Arrays.toString(parts) + "\'");
			nodes.add(new HostAndPort(parts[0], Integer.valueOf(parts[1]).intValue()));
		}

		GenericObjectPoolConfig poolConfig1 = new GenericObjectPoolConfig();
		poolConfig1.setMaxTotal(this.redisClusterProperties.getMaxActive());
		poolConfig1.setMaxIdle(this.redisClusterProperties.getMaxIdle());
		poolConfig1.setMinIdle(this.redisClusterProperties.getMinIdle());
		poolConfig1.setMaxWaitMillis((long) this.redisClusterProperties.getMaxWaitMillis());
		poolConfig1.setTestOnBorrow(true);
		poolConfig1.setTestOnReturn(true);
		return new JedisCluster(nodes, this.redisClusterProperties.getConnectionTimeout(),
				this.redisClusterProperties.getSoTimeout(), this.redisClusterProperties.getMaxAttempts(),
				this.redisClusterProperties.getPassword(), poolConfig1);
	}
	
}
