package net.virtualcraft;

import java.util.logging.Logger;

import net.virtualcraft.control.Window;

public class PublicPropertyZone {

	/** [定数]バージョン */							public static final String APPLICATION_VERSION = "0.1a";

	/** ロガー(ClientApp {version}) */				public static Logger logger;


	/** [参照変数]ウィンドウサイズ(Width) */		public static int intWindowSize_Width;
	/** [参照変数]ウィンドウサイズ(Height) */		public static int intWindowSize_Height;
	/** [参照変数]フルスクリーン判定値 */			public static boolean boolIsFullScreen;


	/** ゲームループの継続判定値 */					public static boolean canRunning;


	/** ゲームウィンドウ */							public static Window gameWindow;
	/** ゲームループ(Mouse)のスレット */			public static MouseThread threadMouse;

}
