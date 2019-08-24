package com.zsk.template.test;

import com.google.common.math.LongMath;
import com.google.common.primitives.Longs;

import java.math.RoundingMode;
import java.util.Arrays;
import java.util.stream.LongStream;

class RedisBitmaps
{

    private static final String BASE_KEY = "bloomfilter";
    private static final String CURSOR = "cursor";

    private JedisUtils jedisUtils;
    private long bitSize;

    RedisBitmaps(long bits)
    {
        this.jedisUtils = new JedisUtils();
        this.bitSize = LongMath.divide(bits, 64, RoundingMode.CEILING) * Long.SIZE;//位数组的长度，相当于n个long的长度
        if (bitCount() == 0)
        {
            jedisUtils.execute((jedis -> jedis.setbit(currentKey(), bitSize - 1, false)));
        }
    }

    boolean get(long[] offsets)
    {
        for (long i = 0; i < cursor() + 1; i++)
        {
            final long cursor = i;
            //只要有一个cursor对应的bitmap中，offsets全部命中，则表示可能存在
            boolean match = Arrays.stream(offsets).boxed().map(offset -> jedisUtils.execute(jedis -> jedis.getbit(genkey(cursor), offset))).allMatch(b -> (Boolean) b);
            if (match)
                return true;
        }
        return false;
    }

    boolean get(final long offset)
    {
        return jedisUtils.execute(jedis -> jedis.getbit(currentKey(), offset));
    }

    boolean set(long[] offsets)
    {
        if (cursor() > 0 && get(offsets))
        {
            return false;
        }
        boolean bitsChanged = false;
        for (long offset : offsets)
            bitsChanged |= set(offset);
        return bitsChanged;
    }

    boolean set(long offset)
    {
        if (!get(offset))
        {
            jedisUtils.execute(jedis -> jedis.setbit(currentKey(), offset, true));
            return true;
        }
        return false;
    }

    long bitCount()
    {
        return jedisUtils.execute(jedis -> jedis.bitcount(currentKey()));
    }

    long bitSize()
    {
        return this.bitSize;
    }

    private String currentKey()
    {
        return genkey(cursor());
    }

    private String genkey(long cursor)
    {
        return BASE_KEY + "-" + cursor;
    }

    private Long cursor()
    {
        String cursor = jedisUtils.execute(jedis -> jedis.get(CURSOR));
        return cursor == null ? 0 : Longs.tryParse(cursor);
    }

    void ensureCapacityInternal()
    {
        if (bitCount() * 2 > bitSize())
            grow();
    }

    void grow()
    {
        Long cursor = jedisUtils.execute(jedis -> jedis.incr(CURSOR));
        jedisUtils.execute((jedis -> jedis.setbit(genkey(cursor), bitSize - 1, false)));
    }

    void reset()
    {
        String[] keys = LongStream.range(0, cursor() + 1).boxed().map(this::genkey).toArray(String[]::new);
        jedisUtils.execute(jedis -> jedis.del(keys));
        jedisUtils.execute(jedis -> jedis.set(CURSOR, "0"));
        jedisUtils.execute(jedis -> jedis.setbit(currentKey(), bitSize - 1, false));
    }

    private PipelineExecutor apply(PipelineExecutor executor)
    {
        return executor;
    }


}