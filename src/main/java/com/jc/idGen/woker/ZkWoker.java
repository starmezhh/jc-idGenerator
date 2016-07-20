package com.jc.idGen.woker;

import com.jc.idGen.zk.ZookeeperClient;
import com.jc.idGen.zk.ZookeeperTransporter;
import com.jc.idGen.zk.lock.ZookeeperLock;
import com.jc.idGen.zk.zkclient.ZkclientZookeeperTransporter;
import org.apache.zookeeper.CreateMode;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by starmezhh on 16/7/18.
 */
public class ZkWoker implements Woker {


    final String WOKERPATH = "/wokerList";

    Long wokerId;

    Long wokerNum = 0L;

    String url;

    String lockName ="woker";

    ZookeeperClient zkClient;

    ZookeeperLock lock;

    String localIp;

    public ZkWoker() {
    }

    public ZkWoker(String url) {
        this.url = url;
    }

    public void init(){
        InetAddress addr = null;
        try {
            addr = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        localIp=addr.getHostAddress();
        //开始获取锁
        lock = new ZookeeperLock(url,lockName);
        //获取锁 阻塞的等待
        lock.lock();
        try{
            if(zkClient==null){
                ZookeeperTransporter zkTransp = new ZkclientZookeeperTransporter();
                zkClient = zkTransp.connect(url);
            }
            //如果wokerList不存在,则创建
            if(!zkClient.exists(WOKERPATH)){
                zkClient.create(WOKERPATH,false);
            }
            //读取childrenNum
            this.wokerNum = Long.valueOf(zkClient.countChildren(WOKERPATH));
            //读取自己IP节点的wokerId
            String localPath = WOKERPATH+"/"+localIp;
            this.wokerId = zkClient.readData(localPath,true);
            //如果机器未注册到zk
            if(wokerId==null){
                wokerId = ++wokerNum;
                zkClient.create(localPath,wokerId, CreateMode.PERSISTENT);
            }
        }finally {
            //释放锁、释放连接
            if(lock!=null)
                lock.unlock();
            if(zkClient!=null)
                zkClient.close();


        }

    }

    public Long getWokerId() {
        return this.wokerId;
    }


    public Long getWokerNum() {
        return this.wokerNum;
    }
}
