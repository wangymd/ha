package ha.service;

import java.util.Set;

/**
 * redis
 * @author wangym
 *
 */
public interface RedisService {

	public void set(String key, Object value);
	
	public void setWithTimeout(String key, Object value, long timeout);
	
	public Object get(String key);
	
	public Boolean delete(String key);
	
	public Set<String> keys(String key);
}
