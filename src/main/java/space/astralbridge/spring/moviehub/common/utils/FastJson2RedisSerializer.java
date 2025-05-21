package space.astralbridge.spring.moviehub.common.utils;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.lang.Nullable;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.filter.Filter;

public class FastJson2RedisSerializer<T> implements RedisSerializer<T> {

    static final Filter AUTO_TYPE_FILTER = JSONReader.autoTypeFilter("space.astralbridge",
            "com.baomidou.mybatisplus");

    private final Class<T> clazz;

    public FastJson2RedisSerializer(Class<T> clazz) {
        super();
        this.clazz = clazz;
    }

    @Override
    public byte[] serialize(@Nullable T t) throws SerializationException {
        if (t == null) {
            return new byte[0];
        }
        return JSON.toJSONBytes(t, JSONWriter.Feature.WriteClassName);
    }

    @Override
    public T deserialize(@Nullable byte[] bytes) throws SerializationException {
        if (ArrayUtils.isEmpty(bytes)) {
            return null;
        }

        return JSON.parseObject(bytes, clazz, AUTO_TYPE_FILTER);
    }
}