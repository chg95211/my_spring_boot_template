package com.zsk.template.test;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @description:
 * @author: zsk
 * @create: 2019-08-24 10:35
 **/
public class RedisBloomFilter
{
    private RedisBitmaps redisBitmaps;
    private int numHashFunctions;

    public RedisBloomFilter(RedisBitmaps redisBitmaps, int numHashFunctions)
    {
        this.redisBitmaps = redisBitmaps;
        this.numHashFunctions = numHashFunctions;
    }

    static int optimalNumOfHashFunctions(long n, long m)
    {
        return Math.max(1, (int) Math.round((double) m / n * Math.log(2)));
    }


    static long optimalNumOfBits(long n, double p)
    {
        if (p == 0)
        {
            p = Double.MIN_VALUE;
        }
        return (long) (-n * Math.log(p) / (Math.log(2) * Math.log(2)));
    }

    public static RedisBloomFilter create(int expectedInsertions, double fpp)
    {
        checkArgument(expectedInsertions >= 0, "Expected insertions (%s) must be >= 0", expectedInsertions);
        checkArgument(fpp > 0.0, "False positive probability (%s) must be > 0.0", fpp);
        checkArgument(fpp < 1.0, "False positive probability (%s) must be < 1.0", fpp);

        if (expectedInsertions == 0)
        {
            expectedInsertions = 1;
        }
        long numBits = optimalNumOfBits(expectedInsertions, fpp);
        int numHashFunctions = optimalNumOfHashFunctions(expectedInsertions, numBits);
        try
        {
            //使用Redis的bitmap实现bit数组
            return new RedisBloomFilter(new RedisBitmaps(numBits), numHashFunctions);
        }
        catch (IllegalArgumentException e)
        {
            throw new IllegalArgumentException("Could not create BloomFilter of " + numBits + " bits", e);
        }
    }

    public boolean put(String string)
    {
        long bitSize = this.redisBitmaps.bitSize();
        byte[] bytes = Hashing.murmur3_128().hashString(string, Charsets.UTF_8).asBytes();
        long hash1 = lowerEight(bytes);
        long hash2 = upperEight(bytes);

        boolean bitsChanged;
        long combinedHash = hash1;
        //        for (int i = 0; i < numHashFunctions; i++) {
        //            bitsChanged |= bits.set((combinedHash & Long.MAX_VALUE) % bitSize);
        //            combinedHash += hash2;
        //        }
        long[] offsets = new long[numHashFunctions];
        for (int i = 0; i < numHashFunctions; i++)
        {
            offsets[i] = (combinedHash & Long.MAX_VALUE) % bitSize;
            combinedHash += hash2;
        }
        bitsChanged = this.redisBitmaps.set(offsets);
        this.redisBitmaps.ensureCapacityInternal();//自动扩容
        return bitsChanged;
    }

    //todo
    private long upperEight(byte[] bytes)
    {
        return 0;
    }

    //todo
    private long lowerEight(byte[] bytes)
    {
        return 0;
    }

    public boolean mightContain(String object)
    {
        long bitSize = this.redisBitmaps.bitSize();
        byte[] bytes = Hashing.murmur3_128().hashString(object, Charsets.UTF_8).asBytes();
        long hash1 = lowerEight(bytes);
        long hash2 = upperEight(bytes);
        long combinedHash = hash1;
        //        for (int i = 0; i < numHashFunctions; i++) {
        //            if (!bits.get((combinedHash & Long.MAX_VALUE) % bitSize)) {
        //                return false;
        //            }
        //            combinedHash += hash2;
        //        }
        //        return true;
        long[] offsets = new long[numHashFunctions];
        for (int i = 0; i < numHashFunctions; i++)
        {
            offsets[i] = (combinedHash & Long.MAX_VALUE) % bitSize;
            combinedHash += hash2;
        }
        return this.redisBitmaps.get(offsets);
    }

    public void resetBitmap()
    {
        this.redisBitmaps.reset();
    }
}
