package net.virtualcraft.server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

public class ServerPropertyZone {

	/** [定数]バージョン */					public static final String APPLICATION_VERSION = "0.1a";

	/** ロガー(ClientApp {version}) */		public static Logger logger;

	/** ゲームループの継続判定値 */			public static boolean canRunning;

	/** クライアントからの接続時に使用*/	public static ServerSocket serverSocket;

	/** リモートサーバ時に使用*/			public static Socket remoteSocket;

	/** サーバスレッド */					public static ServerThread threadServer;
}
