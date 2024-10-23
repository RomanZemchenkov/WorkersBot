package com.roman.dao.redis;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.Set;

@Component
public class RedisRepository {

    private final RedisTemplate<String, String> redisTemplate;
    private static final String[] MEETING_PART= {"participants","date","time","name"};


    public RedisRepository(@Qualifier(value = "redisTemplate") RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    public void saveMeetingPart(String directorId,String partName, String part){
        String tableKey = directorId + "::meeting";
        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
        hashOperations.put(tableKey,partName,part);
        redisTemplate.expire(tableKey, Duration.ofMinutes(10L));
    }

    public String getFullMeetingInfo(String directorId){
        String tableKey = directorId + "::meeting";
        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();

        StringBuilder sb = new StringBuilder();

        for(String part : MEETING_PART){
            Object currentValue = hashOperations.get(tableKey, part);
            sb.append((String) currentValue);
            sb.append(" ");
        }
        return sb.toString();
    }

    public void deleteKey(String keyPattern){
        Set<String> keys = redisTemplate.keys(keyPattern);
        redisTemplate.delete(keys);
    }
}
