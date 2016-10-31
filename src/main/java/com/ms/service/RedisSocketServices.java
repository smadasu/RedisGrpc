package com.ms.service;

import redis.clients.jedis.Jedis;

public class RedisSocketServices {
	
	private final String redisHost;
	private final int redisPort;
	
	public RedisSocketServices(String redisHost, int redisPort) {
		this.redisHost = redisHost;
		this.redisPort = redisPort;
	}
	
	public String get(String key) {
		try(Jedis jedis = new Jedis(redisHost, redisPort)) {
			return jedis.get(key);
		}
	}

}
