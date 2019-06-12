package ha.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ha.service.RedisService;

/**
 * 缓存管理
 * @author wangym
 *
 */
@RestController
@RequestMapping("/cache")
public class CacheController {
	
	@Autowired
	RedisService redisService;
	
	@RequestMapping(path = "/set")
	public void set(@RequestParam(name = "key",required = true) String key, @RequestParam(name = "value",required = true) Object value) {
		redisService.set(key, value);
	}
	
	@RequestMapping(path = "/setWithTimeout")
	public void setWithTimeout(@RequestParam(name = "key",required = true) String key, 
			@RequestParam(name = "value",required = true) Object value,
			@RequestParam(name = "timeout",required = false,defaultValue = "60") long timeout) {
		redisService.setWithTimeout(key, value, timeout);
	}
	
	@RequestMapping(path = "/get")
	public Object get(@RequestParam(name = "key",required = true) String key) {
		return redisService.get(key);
	}
	
	@RequestMapping(path = "/delete")
	public Boolean delete(@RequestParam(name = "key",required = true) String key) {
		return redisService.delete(key);
	}
	
	@RequestMapping(path = "/keys")
	public Set<String> keys(@RequestParam(name = "key",required = false) String key) {
		return redisService.keys(key);
	}
}
