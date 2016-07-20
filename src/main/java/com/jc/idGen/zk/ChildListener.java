package com.jc.idGen.zk;

import java.util.List;

public interface ChildListener {

	void childChanged(String path, List<String> children);

}
