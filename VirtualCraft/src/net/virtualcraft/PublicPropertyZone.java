package net.virtualcraft;

import java.util.HashMap;
import java.util.logging.Logger;

import net.virtualcraft.client.IModEntryPoint;
import net.virtualcraft.control.Window;
import net.virtualcraft.server.RemoteServerThread;

public class PublicPropertyZone {

	/** [定数]バージョン */
	public static final String APPLICATION_VERSION = "0.1a";

	/** ロガー(ClientApp {version}) */
	public static Logger loggerClient;
	/** ロガー(RemoteServerApp {version}) */
	public static Logger loggerRemoteServer;

	/** [参照変数]ウィンドウサイズ(Width) */
	public static int intWindowSize_Width;
	/** [参照変数]ウィンドウサイズ(Height) */
	public static int intWindowSize_Height;
	/** [参照変数]フルスクリーン判定値 */
	public static boolean boolIsFullScreen;
	/** [参照変数]サーバー名 */
	public static String strServerName;
	/** [参照変数]サーバーポート */
	public static int intServerPort;
	/** [参照変数]ワールド名 */
	public static String strWorldName;

	/** ゲームループの継続判定値 */
	public static boolean canRunning;

	/** ゲームウィンドウ */
	public static Window gameWindow;
	/** ゲームループ(Mouse)のスレット */
	public static MouseThread threadMouse;

	/** 最上位ファイルパス */
	public static String strRootPath;

	/** Modエントリーポイント一覧 */
	public static HashMap<String, IModEntryPoint> mapModEP = new HashMap<String, IModEntryPoint>();

	/** リモートサーバスレッド */
	public static RemoteServerThread threadRemoteServer;

}
