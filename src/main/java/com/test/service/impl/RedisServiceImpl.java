package com.test.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;

import com.test.exception.RedisException;
import com.test.service.RedisService;
import com.test.util.SerializingUtil;

import io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue.Supplier;
import redis.clients.jedis.JedisCluster;
public class RedisServiceImpl implements RedisService {
	private static final String JEDIS_SET_RETURN_OK = "OK";
	@Resource
	private JedisCluster jedisCluster;

	public JedisCluster getJedisCluster() {
		return this.jedisCluster;
	}

	public Object getCache(Serializable cacheKey) {
		return SerializingUtil.deserialize((byte[]) this.jedisCluster.get(SerializingUtil.serialize(cacheKey)));
	}

	private byte[] runWithRetries(int attempts, Supplier<byte[]> supplier) {
		if (attempts <= 0) {
			throw new RedisException("Too many Cluster redirections");
		} else {
			try {
				return (byte[]) supplier.get();
			} catch (Exception arg3) {
				return this.runWithRetries(attempts - 1, supplier);
			}
		}
	}

	public boolean putCache(Serializable cacheKey, Object objValue, int expiration) {
		String result = this.jedisCluster.setex(SerializingUtil.serialize(cacheKey), expiration,
				SerializingUtil.serialize(objValue));
		return StringUtils.equals("OK", result);
	}

	public boolean putCache(Serializable cacheKey, Object objValue) {
		String result = this.jedisCluster.set(SerializingUtil.serialize(cacheKey), SerializingUtil.serialize(objValue));
		return StringUtils.equals("OK", result);
	}

	public boolean putnxCache(Serializable cacheKey, Object objValue) {
		return this.jedisCluster.setnx(SerializingUtil.serialize(cacheKey), SerializingUtil.serialize(objValue))
				.longValue() > 0L;
	}

	public Long removeCache(Serializable cacheKey) {
		return this.jedisCluster.del(SerializingUtil.serialize(cacheKey));
	}

	public boolean putListCache(Serializable cacheKey, Object objValue) {
		Long num = this.jedisCluster.rpush(SerializingUtil.serialize(cacheKey),
				new byte[][]{SerializingUtil.serialize(objValue)});
		return num.longValue() > 0L;
	}

	public List<Object> getListCache(Serializable cacheKey, int start, int end) {
		List list = this.jedisCluster.lrange(SerializingUtil.serialize(cacheKey), (long) start, (long) end);
		if (null != list && list.size() > 0) {
			ArrayList objList = new ArrayList();
			Iterator arg5 = list.iterator();

			while (arg5.hasNext()) {
				byte[] b = (byte[]) arg5.next();
				objList.add(SerializingUtil.deserialize(b));
			}

			return objList;
		} else {
			return null;
		}
	}

	public List<Object> getListCache(Serializable cacheKey) {
		return this.getListCache(cacheKey, 0, -1);
	}

	public boolean removeListCache(Serializable cacheKey, Object objValue) {
		long num = this.jedisCluster.lrem(SerializingUtil.serialize(cacheKey), 0L, SerializingUtil.serialize(objValue))
				.longValue();
		return num > 0L;
	}

	public boolean putMapCache(Serializable cacheKey, Map<Object, Object> map) {
		if (null != map && !map.isEmpty()) {
			HashMap byteMap = new HashMap();
			Iterator result = map.entrySet().iterator();

			while (result.hasNext()) {
				Entry entry = (Entry) result.next();
				byteMap.put(SerializingUtil.serialize(entry.getKey()), SerializingUtil.serialize(entry.getValue()));
			}

			String result1 = this.jedisCluster.hmset(SerializingUtil.serialize(cacheKey), byteMap);
			return StringUtils.equals("OK", result1);
		} else {
			return false;
		}
	}

	public boolean deleteMapCache(Serializable cacheKey, Serializable mapKey) {
		Long result = this.jedisCluster.hdel(SerializingUtil.serialize(cacheKey),
				new byte[][]{SerializingUtil.serialize(mapKey)});
		return result.longValue() > 0L;
	}

	public Object getMapValueCache(Serializable cacheKey, Serializable mapKey) {
		List list = this.jedisCluster.hmget(SerializingUtil.serialize(cacheKey),
				new byte[][]{SerializingUtil.serialize(mapKey)});
		return null != list && list.size() > 0 ? SerializingUtil.deserialize((byte[]) list.get(0)) : null;
	}

	public List<Object> getMapMultipleValueCache(Serializable cacheKey, Serializable... mapKey) {
		List list = this.jedisCluster.hmget(SerializingUtil.serialize(cacheKey),
				new byte[][]{SerializingUtil.serialize(mapKey)});
		return null != list && list.size() > 0 ? (List) list.stream().map((item) -> {
			return SerializingUtil.deserialize((byte[]) item);
		}).collect(Collectors.toList()) : null;
	}
}
