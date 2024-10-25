package com.roman.dao.redis;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class RedisRepository {

    private final RedisTemplate<String, String> redisTemplate;
    private static final String[] MEETING_PART= {"participants","date","time","name"};
    private static final String TABLE_MEETING_KEY = "%s::meeting";
    private static final String TABLE_WORKER_NUMBER_KEY = "%s::numbers";


    public RedisRepository(@Qualifier(value = "redisTemplate") RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void saveMeetingPart(String directorId,String partName, String part){
        String tableKey = TABLE_MEETING_KEY.formatted(directorId);
        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
        hashOperations.put(tableKey,partName,part);
        redisTemplate.expire(tableKey, Duration.ofMinutes(10L));
    }

    public String getFullMeetingInfo(String directorId){
        String tableKey = TABLE_MEETING_KEY.formatted(directorId);
        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();

        StringBuilder sb = new StringBuilder();

        for(String part : MEETING_PART){
            Object currentValue = hashOperations.get(tableKey, part);
            sb.append((String) currentValue);
            sb.append(" ");
        }
        return sb.toString();
    }

    public void saveWorkerNumber(String directorId, int workerNumber, String workerId){
        String tableKey = TABLE_WORKER_NUMBER_KEY.formatted(directorId);

        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        hash.put(tableKey,workerNumber,workerId);
        redisTemplate.expire(tableKey,Duration.ofMinutes(10L));

    }

    public Map<String,String> getSavedWorkersNumber(String directorId){
        String tableKey = TABLE_WORKER_NUMBER_KEY.formatted(directorId);

        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        Map<String,String> workersNumberMap = new HashMap<>();
        long countOfNumbers = hash.size(tableKey);
        for(int i = 1; i <= countOfNumbers; i++){
            String position = String.valueOf(i);
            String workerId = (String) hash.get(tableKey, position);
            workersNumberMap.put(position,workerId);
        }
        deleteKey(tableKey);
        return workersNumberMap;
    }

    public void deleteKey(String keyPattern){
        Set<String> keys = redisTemplate.keys(keyPattern);
        redisTemplate.delete(keys);
    }
}
