package net.virtualcraft;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.virtualcraft.control.Window;
import net.virtualcraft.server.ServerStart;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

public class ClientStart {

	/** @param args {width, height, isfull, IPAddr(+Port) | FQDN(+Port), WorldName, PlayerName, PlayerPass}*/
	public static void main(String[] args) {

		//Loggerの作成
		Logger logger = PublicPropertyZone.logger = Logger.getLogger("ClientApp " + PublicPropertyZone.APPLICATION_VERSION);
		logger.setUseParentHandlers(false);	//親に通知しない

		//コンソールハンドラの作成＆設定
		ConsoleHandler handler = new ConsoleHandler();
		handler.setFormatter(new CustomLogFormatter());
		handler.setLevel(Level.ALL);
		logger.setLevel(Level.ALL);
		logger.addHandler(handler);

		logger.info("------------ CLIENT START ------------");
		if(args.length < 5) {
			logger.info("引数が足りないため起動に失敗しました。");
		} else {
			PublicPropertyZone.intWindowSize_Width	= Integer.parseInt(args[0]);
			PublicPropertyZone.intWindowSize_Height	= Integer.parseInt(args[1]);
			PublicPropertyZone.boolIsFullScreen = Boolean.parseBoolean(args[2]);
			//  ウインドウを生成する
			try {
				Display.setDisplayMode(new DisplayMode(PublicPropertyZone.intWindowSize_Width, PublicPropertyZone.intWindowSize_Height));
				Display.setFullscreen(PublicPropertyZone.boolIsFullScreen);
				Display.setTitle("VirtualCraft ver." + PublicPropertyZone.APPLICATION_VERSION);
				Display.create();

				PublicPropertyZone.gameWindow = new Window(0, 0, PublicPropertyZone.intWindowSize_Width, PublicPropertyZone.intWindowSize_Height);

				logger.info(String.format("ウィンドウ生成に成功しました。(%d x %d) WindowMode：%s", PublicPropertyZone.intWindowSize_Width, PublicPropertyZone.intWindowSize_Height, !PublicPropertyZone.boolIsFullScreen));
			} catch(LWJGLException e) {
				logger.warning("ウィンドウ生成に失敗しました。");
				e.printStackTrace();
				return;
			}

			{//サーバへ接続
				if(! ServerStart.Init(new String[]{ args[3], args[4]})) {
					PublicPropertyZone.threadMouse.Stop();
					Display.destroy();
					logger.info(String.format("ウィンドウ破棄に成功しました。(%d x %d) WindowMode：%s", PublicPropertyZone.intWindowSize_Width, PublicPropertyZone.intWindowSize_Height, !PublicPropertyZone.boolIsFullScreen));
				}
			}

			//描画用意
			GL11.glEnable(GL11.GL_CULL_FACE);
			GL11.glCullFace(GL11.GL_BACK);
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();
			GL11.glOrtho(0, PublicPropertyZone.intWindowSize_Width, 0, PublicPropertyZone.intWindowSize_Height, 0, 100);
			GL11.glMatrixMode(GL11.GL_MODELVIEW);

			//スレッドの生成
			PublicPropertyZone.threadMouse = new MouseThread(PublicPropertyZone.gameWindow);
			PublicPropertyZone.threadMouse.Start();

			PublicPropertyZone.canRunning = true;
			while(PublicPropertyZone.canRunning) {
				if(Display.isCloseRequested()) break;	//ウィンドウの「x」ボタンが押された場合
				Display.update();	//ディスプレイの更新

				if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) { //デバッグ用キー処理「Esc」でゲーム終了
					PublicPropertyZone.canRunning = false; continue;
				}

				GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

				GL11.glBegin(GL11.GL_QUADS);

				//  OpenGL では頂点が左回りになっているのがポリゴンの表となる
				//  今は表のみ表示する設定にしているので、頂点の方向を反対にすると裏側となり、表示されなくなる

				GL11.glColor3f(1.0f, 0.5f, 0.5f);            //  次に指定する座標に RGB で色を設定する
				GL11.glVertex3f(PublicPropertyZone.intWindowSize_Width - 50, PublicPropertyZone.intWindowSize_Height- 50, 0);  //  1 つめの座標を指定する

				GL11.glColor3f(0.5f, 1.0f, 0.5f);
				GL11.glVertex3f(50, PublicPropertyZone.intWindowSize_Height - 50, 0);      // 2 つめの座標を指定する

				GL11.glColor3f(0.5f, 0.5f, 1.0f);
				GL11.glVertex3f(50, 50, 0);                //    3 つめの座標を指定する

				GL11.glColor3f(1.0f, 0.5f, 0.5f);
				GL11.glVertex3f(PublicPropertyZone.intWindowSize_Width - 50, 50, 0);        //    4 つめの座標を指定する

				GL11.glEnd();

				try {
					Thread.currentThread().sleep(16);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

//			threadDraw[0].Stop();

			PublicPropertyZone.threadMouse.Stop();
			Display.destroy();
			logger.info(String.format("ウィンドウ破棄に成功しました。(%d x %d) WindowMode：%s", PublicPropertyZone.intWindowSize_Width, PublicPropertyZone.intWindowSize_Height, !PublicPropertyZone.boolIsFullScreen));
		}
	}

}
