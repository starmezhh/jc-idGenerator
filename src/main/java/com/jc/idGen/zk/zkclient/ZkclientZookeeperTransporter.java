package com.jc.idGen.zk.zkclient;


import com.jc.idGen.zk.ZookeeperClient;
import com.jc.idGen.zk.ZookeeperTransporter;

public class ZkclientZookeeperTransporter implements ZookeeperTransporter {

	public ZookeeperClient connect(String url) {
		return new ZkclientZookeeperClient(url);
	}

}
