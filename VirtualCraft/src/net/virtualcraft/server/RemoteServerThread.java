package net.virtualcraft.server;

import net.virtualcraft.util.FPSWatcher;

public class RemoteServerThread implements Runnable {

	private Thread thread;
	public boolean isRunning;

	private FPSWatcher watcher;

	public RemoteServerThread(String worldName) {
		thread = new Thread(this, "RemoteServerThread");
		watcher = new FPSWatcher(90, "RemoteServerThread");
	}

	@Override
	public void run() {
		while(isRunning) {
			watcher.PreTick();
			long sleepTime = watcher.TickProgress();
			if(sleepTime > 0) {
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			watcher.TickSleep();
			watcher.Update();
		}
	}

	public void Start() { isRunning = true; thread.start(); }

	public void Stop() { isRunning = false; }
}
