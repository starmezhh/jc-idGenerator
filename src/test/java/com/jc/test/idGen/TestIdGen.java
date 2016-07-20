package com.jc.test.idGen;

import com.jc.idGen.gen.IdGenerator;
import com.jc.idGen.gen.ZkIdGenerator;
import com.jc.idGen.woker.Woker;
import com.jc.idGen.woker.ZkWoker;
import com.jc.idGen.zk.ZookeeperClient;
import com.jc.idGen.zk.ZookeeperTransporter;
import com.jc.idGen.zk.zkclient.ZkclientZookeeperTransporter;
import org.junit.Test;

import java.util.Date;
import java.util.List;

/**
 * Created by starmezhh on 16/7/19.
 */

public class TestIdGen {


    @Test
    public void testZK(){
        ZookeeperTransporter zkTransp = new ZkclientZookeeperTransporter();
        ZookeeperClient zkClient = zkTransp.connect("192.168.10.217:2181");
        List<String> list = zkClient.getChildren("/");
        System.out.println(list);
    }


    @Test
    public void testWoker(){
        Woker woker = new ZkWoker("192.168.10.217:2181");
        woker.init();
        System.out.println(woker.getWokerId());
        System.out.println(woker.getWokerNum());
    }

    @Test
    public void testGen(){
        Woker woker = new ZkWoker("192.168.10.217:2181");
        woker.init();
        IdGenerator idGenerator = new ZkIdGenerator(woker);
        idGenerator.init();
        Long t1 = new Date().getTime();
        int num = 0;
        try {
            while(new Date().getTime()-t1<1000){
                num++;
                System.out.println(idGenerator.genLongId()+"");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println(num);
    }

    public void testGenOne(){
        Woker woker = new ZkWoker("192.168.10.217:2181");
        woker.init();
        IdGenerator idGenerator = new ZkIdGenerator(woker);
        idGenerator.init();
        try {
            System.out.println(idGenerator.genLongId()+"");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
