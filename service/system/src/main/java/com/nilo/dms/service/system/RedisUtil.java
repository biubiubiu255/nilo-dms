package com.nilo.dms.service.system;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Set;

/**
 * Created by admin on 2017/10/9.
 */
public class RedisUtil {

    private static JedisPool jedisPool = SpringContext.getBean("jedisPool",JedisPool.class);

    public static Jedis getRedisClient() {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis;
        } catch (Exception e) {
            if (null != jedis)
                jedis.close();
        }
        return null;
    }

    public static void returnResource(Jedis jedis) {
        jedis.close();
    }

    public static void set(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.set(key, value);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    public static String get(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String value = jedis.get(key);
            return value;
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    public static void expire(String key, int time) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.expire(key, time);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    public static Long increment(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.incr(key);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    public static void hset(String key, String field, String value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.hset(key, field, value);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    public static String hget(String key, String field) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.hget(key, field);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    public static Set<String> keys(String pattern) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.keys(pattern);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    public static void del(String... keys) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.del(keys);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    public static void srem(String key, String... value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.srem(key, value);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    public static void sadd(String key, String... value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.sadd(key, value);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    public static Set<String> smembers(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.smembers(key);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    public static void lpush(String name, String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.lpush(name, key);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    public static String rpop(String name) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.rpop(name);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }
}
