package com.jc.idGen.zk.support;

import com.jc.idGen.zk.ChildListener;
import com.jc.idGen.zk.StateListener;
import com.jc.idGen.zk.ZookeeperClient;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArraySet;

public abstract class AbstractZookeeperClient<TargetChildListener> implements ZookeeperClient {

	protected static final Logger logger = Logger.getLogger(AbstractZookeeperClient.class);

	private final String url;

	private final Set<StateListener> stateListeners = new CopyOnWriteArraySet<StateListener>();

	private final ConcurrentMap<String, ConcurrentMap<ChildListener, TargetChildListener>> childListeners = new ConcurrentHashMap<String, ConcurrentMap<ChildListener, TargetChildListener>>();

	private volatile boolean closed = false;

	public AbstractZookeeperClient(String url) {
		this.url = url;
	}

//	public URL getUrl() {
//		return url;
//	}

	public void create(String path, boolean ephemeral) {
		int i = path.lastIndexOf('/');
		if (i > 0) {
			create(path.substring(0, i), false);
		}
		if (ephemeral) {
			createEphemeral(path);
		} else {
			createPersistent(path);
		}
	}

	public void addStateListener(StateListener listener) {
		stateListeners.add(listener);
	}

	public void removeStateListener(StateListener listener) {
		stateListeners.remove(listener);
	}

	public Set<StateListener> getSessionListeners() {
		return stateListeners;
	}

	public List<String> addChildListener(String path, final ChildListener listener) {
		ConcurrentMap<ChildListener, TargetChildListener> listeners = childListeners.get(path);
		if (listeners == null) {
			childListeners.putIfAbsent(path, new ConcurrentHashMap<ChildListener, TargetChildListener>());
			listeners = childListeners.get(path);
		}
		TargetChildListener targetListener = listeners.get(listener);
		if (targetListener == null) {
			listeners.putIfAbsent(listener, createTargetChildListener(path, listener));
			targetListener = listeners.get(listener);
		}
		return addTargetChildListener(path, targetListener);
	}

	public void removeChildListener(String path, ChildListener listener) {
		ConcurrentMap<ChildListener, TargetChildListener> listeners = childListeners.get(path);
		if (listeners != null) {
			TargetChildListener targetListener = listeners.remove(listener);
			if (targetListener != null) {
				removeTargetChildListener(path, targetListener);
			}
		}
	}

	protected void stateChanged(int state) {
		for (StateListener sessionListener : getSessionListeners()) {
			sessionListener.stateChanged(state);
		}
	}

	public void close() {
		if (closed) {
			return;
		}
		closed = true;
		try {
			doClose();
		} catch (Throwable t) {
			logger.warn(t.getMessage(), t);
		}
	}

	protected abstract void doClose();

	public abstract void createPersistent(String path);

	public abstract void createEphemeral(String path);

	public abstract void createEphemeralSequential(String path,Object object);

	public abstract void createPersistentSequential(String path);

	protected abstract TargetChildListener createTargetChildListener(String path, ChildListener listener);

	protected abstract List<String> addTargetChildListener(String path, TargetChildListener listener);

	protected abstract void removeTargetChildListener(String path, TargetChildListener listener);

}