package com.jc.idGen.gen;

import com.jc.idGen.exception.InvalidSystemClockException;
import com.jc.idGen.woker.Woker;

/**
 * Created by starmezhh on 16/7/18.
 */
public class ZkIdGenerator extends BaseIdGenerator {

    Long wokerNum;

    Woker woker;

    public ZkIdGenerator() {
    }

    public ZkIdGenerator(Woker woker) {
        this.woker = woker;
    }

    public void init(){
        this.wokerId = woker.getWokerId();
        this.wokerNum = woker.getWokerNum();
    }

    public void setWoker(Woker woker) {
        this.woker = woker;
    }

    public synchronized Long genLongId() throws Exception {
        long timestamp = System.currentTimeMillis();
        //NTP时钟检查
        if (timestamp < lastTimestamp) {
            throw new InvalidSystemClockException("Clock moved backwards.  Refusing to generate id for " + (
                    lastTimestamp - timestamp) + " milliseconds.");
        }
        if (lastTimestamp == timestamp) {
                sequence = (sequence + 1) % sequenceMax;
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            //避免经常出现某一毫秒内只有1-2个请求,造成取模分布不均,对初始的
            //应该使用wokerNum,但由于未经zookeeper进行同步,所以暂时不用
            sequence = initSeq(10L);
        }
        lastTimestamp = timestamp;
        Long id = ((timestamp - twepoch) << timestampLeftShift) |
                (wokerId << datacenterIdShift) |
                sequence;
        return id;
    }

}
