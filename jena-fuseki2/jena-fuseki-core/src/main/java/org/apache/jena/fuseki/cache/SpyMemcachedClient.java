package org.apache.jena.fuseki.cache;

import net.spy.memcached.MemcachedClient;
import org.apache.jena.fuseki.Fuseki;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class SpyMemcachedClient extends CacheClient{


    private static Logger log = Fuseki.cacheLog;

    /** The MemcachedClient object **/
    private MemcachedClient cache;

    /** How long cache data will be stored after write **/
    private static int EXPIRE_TIME = 300;

    public SpyMemcachedClient(){
        try {
            cache = new MemcachedClient(new InetSocketAddress("localhost", 11211));
        }catch (IOException ex){
            throw new CacheStoreException("CACHE CONNECT FAILED", ex);
        }
    }
    @Override
    public Object get(Object key) throws InterruptedException, ExecutionException, TimeoutException {
        log.info("key "+(String) key);
        return cache.get((String) key);
    }

    @Override
    public boolean set(Object key, Object value) throws InterruptedException, ExecutionException, TimeoutException {
        cache.set((String) key, EXPIRE_TIME, value);
        return true;
    }

    @Override
    public boolean unset(Object key) throws InterruptedException, ExecutionException, TimeoutException {
        cache.delete((String) key);
        return true;
    }
}
