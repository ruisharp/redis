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
		JedisCluster.set("key-test", "c��");
		System.out.println(JedisCluster.get("key-test"));
		 /** 
	     * �洢���ݵ������У����ƶ�����ʱ��͵�Key����ʱ�Ƿ񸲸ǡ� 
	     * 
	     * @param key 
	     * @param value 
	     * @param nxxx 
	     *            nxxx��ֵֻ��ȡNX����XX�����ȡNX����ֻ�е�key������ʱ�Ž���set�����ȡXX����ֻ�е�key�Ѿ�����ʱ�Ž���set 
	     * 
	     * @param expx expx��ֵֻ��ȡEX����PX���������ݹ���ʱ��ĵ�λ��EX�����룬PX������롣 
	     * @param time ����ʱ�䣬��λ��expx������ĵ�λ�� 
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
