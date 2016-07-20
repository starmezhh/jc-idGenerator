package com.jc.idGen.zk;


//@SPI("zkclient")
public interface ZookeeperTransporter {

//	@Adaptive({Constants.CLIENT_KEY, Constants.TRANSPORTER_KEY})
	ZookeeperClient connect(String url);

}
