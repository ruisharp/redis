package com.test.util;

import java.io.Serializable;

public class RedisParam<V> implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String prefix;
	private String key;
	private V value;
	private String[] keyParam;
	private int expiration;
	private int step;

	protected RedisParam(String prefix, String key, V value, int step, int expiration, String... keyParam) {
		this.prefix = prefix;
		this.key = key;
		this.value = value;
		this.step = step;
		this.keyParam = keyParam;
		this.expiration = expiration;
	}

	public String getKey() {
		return this.key;
	}

	public int getStep() {
		return this.step;
	}

	public void setStep(int step) {
		this.step = step;
	}

	public String getPrefix() {
		return this.prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public byte[] getRedisKey() {
		return SerializingUtil.generateKey(this.prefix, this.key, this.keyParam);
	}

	public void setKey(String key) {
		this.key = key;
	}

	public V getValue() {
		return this.value;
	}

	public void setValue(V value) {
		this.value = value;
	}

	public String[] getKeyParam() {
		return this.keyParam;
	}

	public void setKeyParam(String[] keyParam) {
		this.keyParam = keyParam;
	}

	public int getExpiration() {
		return this.expiration;
	}

	public void setExpiration(int expiration) {
		this.expiration = expiration;
	}

	public static RedisParam of(String prefix, String key, String... keyParam) {
		return new RedisParam(prefix, key, (Object) null, 0, 0, keyParam);
	}

	public static RedisParam of(String prefix, String key, int step, String... keyParam) {
		return new RedisParam(prefix, key, (Object) null, step, 0, keyParam);
	}

	public static <V> RedisParam<V> of(String prefix, String key, V value, String... keyParam) {
		return new RedisParam(prefix, key, value, 0, 0, keyParam);
	}

	public static <V> RedisParam<V> of(String prefix, String key, V value, int exp, String... keyParam) {
		return new RedisParam(prefix, key, value, 0, exp, keyParam);
	}
}