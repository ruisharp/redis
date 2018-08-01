package com.test.util;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.test.service.RedisService;



public class RedisCacheUtil {
	private static final Logger log = LoggerFactory.getLogger(RedisCacheUtil.class);
	@Autowired
	RedisService redisService;

	public boolean setValue(RedisParam redisParam) {
		return redisParam.getExpiration() <= 0
				? this.redisService.putCache(redisParam.getRedisKey(), redisParam.getValue())
				: this.redisService.putCache(redisParam.getRedisKey(), redisParam.getValue(),
						redisParam.getExpiration());
	}

	public boolean setnxValue(RedisParam redisParam) {
		return this.redisService.putnxCache(redisParam.getRedisKey(), redisParam.getValue());
	}

	public long incrOne(RedisParam redisParam) {
		return this.redisService.getJedisCluster().incr(redisParam.getRedisKey()).longValue();
	}

	public long incrStep(RedisParam redisParam) {
		return this.redisService.getJedisCluster().incrBy(redisParam.getRedisKey(), (long) redisParam.getStep())
				.longValue();
	}

	public long decrStep(RedisParam redisParam) {
		return this.redisService.getJedisCluster().decrBy(redisParam.getRedisKey(), (long) redisParam.getStep())
				.longValue();
	}

	public boolean exists(RedisParam redisParam) {
		return this.redisService.getJedisCluster().exists(redisParam.getRedisKey()).booleanValue();
	}

	public boolean expire(RedisParam redisParam) {
		return this.redisService.getJedisCluster().expire(redisParam.getRedisKey(), redisParam.getExpiration())
				.longValue() >= 0L;
	}

	public Object getValue(RedisParam redisParam) {
		return this.redisService.getCache(redisParam.getRedisKey());
	}

	public boolean remove(RedisParam redisParam) {
		return this.redisService.removeCache(redisParam.getRedisKey()).longValue() > 0L;
	}

	public boolean rightPush(RedisParam redisParam) {
		return this.redisService.putListCache(redisParam.getRedisKey(), redisParam.getValue());
	}

	public Long getListSize(RedisParam redisParam) {
		return this.redisService.getJedisCluster().llen(SerializingUtil.serialize(redisParam.getRedisKey()));
	}

	public boolean removeFromList(RedisParam redisParam) {
		return this.redisService.removeListCache(redisParam.getRedisKey(), redisParam.getValue());
	}

	public <T> List<T> getList(RedisParam redisParam) {
		return this.redisService.getListCache(redisParam.getRedisKey());
	}

	public <T> T getOrSetValue(RedisParam redisParam, Supplier<T> supplier) {
		Object t = this.getValue(redisParam);
		t = Optional.ofNullable(t).orElseGet(() -> {
			Object t1 = supplier.get();
			redisParam.setValue(t1);
			this.setValue(redisParam);
			return t1;
		});
		return t;
	}

	public boolean lock(String key) {
		long begin = System.nanoTime();
		return "OK".equalsIgnoreCase(
				this.redisService.getJedisCluster().set(key, String.valueOf(begin), "nx", "ex", 300L));
	}

	public boolean lock(String key, int timeout) {
		long begin = System.nanoTime();

		try {
			do {
				String e = this.redisService.getJedisCluster().set(key, String.valueOf(begin), "nx", "ex",
						(long) (timeout + 1));
				if ("OK".equalsIgnoreCase(e)) {
					return true;
				}

				TimeUnit.MILLISECONDS.sleep(100L);
			} while (System.nanoTime() - begin < TimeUnit.SECONDS.toNanos((long) timeout));
		} catch (Exception arg5) {
			log.error("------->>>>>lock key=" + key + " fail......", arg5);
		}

		return false;
	}

	public <T> T lockOneMinute(String key, Supplier<T> supplier) {
		return this.lock(key, 60, supplier);
	}

	public <T> T lock(String key, int timeout, Supplier<T> supplier) {
		long begin = System.nanoTime();
		boolean lock = false;

		try {
			do {
				String e = this.redisService.getJedisCluster().set(key, String.valueOf(begin), "nx", "ex",
						(long) (timeout + 1));
				if ("OK".equalsIgnoreCase(e)) {
					lock = true;
					break;
				}

				TimeUnit.MILLISECONDS.sleep(100L);
			} while (System.nanoTime() - begin < TimeUnit.SECONDS.toNanos((long) timeout));

			if (lock) {
				Object e1 = supplier.get();
				return e1;
			}
		} catch (Exception arg10) {
			log.error("------->>>>>lock key=" + key + " fail......", arg10);
		} finally {
			if (lock) {
				this.unlock(key);
			}

		}

		return null;
	}

	public boolean unlock(String key) {
		return this.redisService.getJedisCluster().del(key).longValue() > 0L;
	}
}