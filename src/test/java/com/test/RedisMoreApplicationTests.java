package com.test;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.test.util.RedisCacheUtil;
import com.test.util.RedisParam;

import redis.clients.jedis.JedisCluster;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisMoreApplicationTests {
	
	@Resource
	JedisCluster JedisCluster;
	@Resource
	RedisCacheUtil redisCacheUtil;

	@Test
	public void contextLoads() {
		JedisCluster.set("key-test", "c罗");
		System.out.println(JedisCluster.get("key-test"));
		 /** 
	     * 存储数据到缓存中，并制定过期时间和当Key存在时是否覆盖。 
	     * 
	     * @param key 
	     * @param value 
	     * @param nxxx 
	     *            nxxx的值只能取NX或者XX，如果取NX，则只有当key不存在时才进行set，如果取XX，则只有当key已经存在时才进行set 
	     * 
	     * @param expx expx的值只能取EX或者PX，代表数据过期时间的单位，EX代表秒，PX代表毫秒。 
	     * @param time 过期时间，单位是expx所代表的单位。 
	     * @return  ok or null
	     */  
		long begin = System.nanoTime();
		String s =  JedisCluster.set("key-test-1", String.valueOf(begin), "nx", "ex", 1l);
		System.out.println(s);
		String ss =  JedisCluster.set("key-test-1", String.valueOf(begin), "nx", "ex", 1l);
		System.out.println(ss);
		RedisParam redisParam = RedisParam.of(prefix, key, keyParam);
		redisCacheUtil.setValue(redisParam);
	}

}
