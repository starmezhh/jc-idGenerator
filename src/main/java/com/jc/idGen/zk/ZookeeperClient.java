package com.jc.idGen.zk;


import org.apache.zookeeper.CreateMode;

import java.util.List;

public interface ZookeeperClient {

	void create(String path, boolean ephemeral);

	void delete(String path);

	int countChildren(String path);

	List<String> getChildren(String path);

	List<String> addChildListener(String path, ChildListener listener);

	void removeChildListener(String path, ChildListener listener);

	void addStateListener(StateListener listener);
	
	void removeStateListener(StateListener listener);

	boolean isConnected();

	void close();

	boolean exists(String path);

	<T extends Object> T readData(String path, boolean returnNullIfPathNotExists);

	String create(final String path, Object data, final CreateMode mode);

}
