package net.virtualcraft.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.virtualcraft.PublicPropertyZone;
import net.virtualcraft.util.CustomLogFormatter;
import net.virtualcraft.util.FPSWatcher;

public class RemoteServerThread implements Runnable {


	private ServerSocket serverSocket;
	private Socket remoteSocket;

	private Thread thread;
	public boolean isRunning;

	private FPSWatcher watcher;

	public RemoteServerThread(String serverName, int serverPort, String worldName) {

		//Loggerの作成
		PublicPropertyZone.loggerRemoteServer = Logger.getLogger("RemoteServerApp " + PublicPropertyZone.APPLICATION_VERSION);
		PublicPropertyZone.loggerRemoteServer.setUseParentHandlers(false);	//親に通知しない

		//コンソールハンドラの作成＆設定
		ConsoleHandler handler = new ConsoleHandler();
		handler.setFormatter(new CustomLogFormatter());
		handler.setLevel(Level.ALL);
		PublicPropertyZone.loggerRemoteServer.setLevel(Level.ALL);
		PublicPropertyZone.loggerRemoteServer.addHandler(handler);

		thread = new Thread(this, "RemoteServerThread");
		watcher = new FPSWatcher(90, "RemoteServerThread", PublicPropertyZone.loggerRemoteServer);

		PublicPropertyZone.strServerName = serverName;
		PublicPropertyZone.intServerPort = serverPort;
		PublicPropertyZone.strWorldName = worldName;
	}

	@Override
	public void run() {
		try {
			serverSocket = new ServerSocket(PublicPropertyZone.intServerPort);
			remoteSocket = new Socket(PublicPropertyZone.strServerName, PublicPropertyZone.intServerPort);
			PublicPropertyZone.loggerRemoteServer.info(String.format("リモートサーバの設立に成功しました。Server(%s %d) World(%s)",
					PublicPropertyZone.strServerName, PublicPropertyZone.intServerPort, PublicPropertyZone.strWorldName));
		} catch (IOException e) {
			PublicPropertyZone.loggerRemoteServer.info(String.format("リモートサーバの設立に失敗しました。Server(%s %d) World(%s)",
					PublicPropertyZone.strServerName, PublicPropertyZone.intServerPort, PublicPropertyZone.strWorldName));
			return;
		}

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
