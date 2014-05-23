package org.springframework.cache.memcached;

import net.spy.memcached.MemcachedClient;
import net.spy.memcached.compat.log.Logger;
import net.spy.memcached.compat.log.LoggerFactory;

import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;

public class MemcachedCache implements Cache {

    private static final Logger LOG = LoggerFactory.getLogger(MemcachedCache.class);

    private String name;
    private MemcachedClient client;
    private int expiry = 3600;

    public MemcachedCache() {}

    public MemcachedCache(String name, MemcachedClient client, int expiry) {
        this.name = name;
        this.client = client;
        this.expiry = expiry;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Object getNativeCache() {
        return client;
    }

    @Override
    public ValueWrapper get(Object key) {
        if (client.getAvailableServers().size() == 0) {
            // If memcached server is unavailable then just return null.
            // Ideally, the actually function would be executing without caching mechanism.
            // However, do check the memcached server if the warning occurred.
            LOG.warn("Memcached server is unavailable");
            return null;
        }

        Object value = client.get(keyToString(key));
        return value != null ? new SimpleValueWrapper(value) : null;
    }

    @Override
    public void put(Object key, Object value) {
        this.client.add(keyToString(key), expiry, value);
    }

    @Override
    public void evict(Object key) {
        this.client.delete(keyToString(key));
    }

    @Override
    public void clear() {
        // TODO
    }

    public void setClient(MemcachedClient client) {
        this.client = client;
    }

    public void setExpiry(int expiry) {
        this.expiry = expiry;
    }

    public void setName(String name) {
        this.name = name;
    }

    private static String keyToString(Object key) {
        if (key == null) {
            return null;
        } else if (key instanceof String) {
            return (String) key;
        } else {
            return key.toString();
        }
    }
}
