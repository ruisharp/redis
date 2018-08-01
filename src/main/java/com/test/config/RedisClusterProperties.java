package com.test.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
@Configuration
@ConfigurationProperties(prefix = "spring.redis.cluster")
public class RedisClusterProperties {
	private List<String> nodes = new ArrayList<String>();
	private String password;
	private int connectionTimeout = 5000;
	private int soTimeout = 5000;
	private int maxAttempts = 3;
	private int maxActive = 20;
	private int maxIdle = 20;
	private int minIdle = 5;
	private int maxWaitMillis = 10000;

	public int getMaxActive() {
		return this.maxActive;
	}

	public void setMaxActive(int maxActive) {
		this.maxActive = maxActive;
	}

	public int getMaxIdle() {
		return this.maxIdle;
	}

	public void setMaxIdle(int maxIdle) {
		this.maxIdle = maxIdle;
	}

	public int getMinIdle() {
		return this.minIdle;
	}

	public void setMinIdle(int minIdle) {
		this.minIdle = minIdle;
	}

	public int getMaxWaitMillis() {
		return this.maxWaitMillis;
	}

	public void setMaxWaitMillis(int maxWaitMillis) {
		this.maxWaitMillis = maxWaitMillis;
	}

	public int getConnectionTimeout() {
		return this.getTimeOut(this.connectionTimeout);
	}

	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	public int getSoTimeout() {
		return this.getTimeOut(this.soTimeout);
	}

	public void setSoTimeout(int soTimeout) {
		this.soTimeout = soTimeout;
	}

	public int getMaxAttempts() {
		return this.maxAttempts;
	}

	public void setMaxAttempts(int maxAttempts) {
		this.maxAttempts = maxAttempts;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<String> getNodes() {
		return this.nodes;
	}

	public void setNodes(List<String> nodes) {
		this.nodes = nodes;
	}

	private int getTimeOut(int time) {
		return time < 5000 ? 5000 : time;
	}
}
