package com.test.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import redis.clients.jedis.JedisCluster;

public interface RedisService {
	JedisCluster getJedisCluster();

	Object getCache(Serializable arg0);

	boolean putCache(Serializable arg0, Object arg1, int arg2);

	boolean putCache(Serializable arg0, Object arg1);

	boolean putnxCache(Serializable arg0, Object arg1);

	Long removeCache(Serializable arg0);

	boolean putListCache(Serializable arg0, Object arg1);

	List<Object> getListCache(Serializable arg0, int arg1, int arg2);

	List<Object> getListCache(Serializable arg0);

	boolean removeListCache(Serializable arg0, Object arg1);

	boolean putMapCache(Serializable arg0, Map<Object, Object> arg1);

	boolean deleteMapCache(Serializable arg0, Serializable arg1);

	Object getMapValueCache(Serializable arg0, Serializable arg1);

	List<Object> getMapMultipleValueCache(Serializable arg0, Serializable... arg1);
}
