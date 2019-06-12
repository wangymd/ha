package ha.service.impl;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import ha.service.RedisService;

/**
 * 
 * @author wangym
 *
 */
@Service
@SuppressWarnings({"rawtypes","unchecked"})
public class RedisServiceImpl implements RedisService {
	
	@Autowired
	RedisTemplate redisTemplate;
	
	@Autowired
	StringRedisTemplate stringRedisTemplate;

	@Override
	public void set(String key, Object value) {
		try {
			redisTemplate.opsForValue().set(key, value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void setWithTimeout(String key, Object value, long timeout) {
		try {
			redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Object get(String key) {
		try {
			return redisTemplate.opsForValue().get(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Boolean delete(String key) {
		try {
			return redisTemplate.delete(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public Set<String> keys(String key) {
		try {
			if(StringUtils.isEmpty(key)) key = "";
			return redisTemplate.keys(key + "*");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
