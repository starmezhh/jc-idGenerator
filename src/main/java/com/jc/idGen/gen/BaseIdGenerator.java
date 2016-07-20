package com.jc.idGen.gen;

import java.util.Random;

/**
 * Created by starmezhh on 16/7/18.
 */
public abstract class BaseIdGenerator implements IdGenerator {


    //当前机器分配的wokerId
    protected long wokerId;

    //   id format  =>
    //   timestamp |datacenter | sequence
    //   41        |10         |  12
    protected final long sequenceBits = 12L;
    protected final long datacenterIdBits = 10L;

    //wokerId最大值
    protected final long maxDataCenterId = -1L ^ (-1L << datacenterIdBits);

    //wokerId偏移量
    protected final long datacenterIdShift = sequenceBits;
    //时间戳的偏移量
    protected final long timestampLeftShift = sequenceBits + datacenterIdBits;

    // 基准时间 Thu, 04 Nov 2010 01:42:54 GMT
    protected final long twepoch = 1288834974657L;
    //序列最大值
    protected final long sequenceMax = 4096;


    protected volatile long lastTimestamp = -1L;
    protected volatile long sequence = 0L;

    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = System.currentTimeMillis();
        while (timestamp <= lastTimestamp) {
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
    }

    protected long initSeq(Long limit){
        if(limit==null)
            return new Random().nextLong();
        else
            return new Random().nextInt(limit.intValue());
    }


}
