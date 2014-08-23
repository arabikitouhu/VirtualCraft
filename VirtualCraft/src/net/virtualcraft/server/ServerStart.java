package net.virtualcraft.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.virtualcraft.CustomLogFormatter;

public class ServerStart {

	/** @param args { IPAddr(+Port) | FQDN(+Port), WorldName }*/
	public static void main(String[] args) {

		/*	仕様 -----------------------------------------------------------------------------------------------------
		 *	IPAddr == "127.0.0.1"			//ループバックアドレスの場合は、サーバ起動
		 *	IPAddr == "AAA.BBB.CCC.DDD"		//アドレスがそれ以外の場合は、リモートサーバ起動
		 *	※IPAddrにPortがついていない場合は、自動で付与( 12525)
		 *
		 *	FQDNの場合は、必ずリモートサーバ
		 *	※FQDNにPortがついていない場合は、自動で付与( 12525)
		 *
		 *	※Port指定は、先頭に「空白(小文字)」を付けた数値で指定できる。
		 *	----------------------------------------------------------------------------------------------------------
		 *	サーバの役割 ---------------------------------------------------------------------------------------------
		 *	１．ワールドデータのロード
		 *	２．データのコミット(反映がある場合 ※たとえば、ボタンを押した通知を投げるなど)
		 *	３．接続されているリモートサーバへコミットしたデータの反映分の同期
		 *	----------------------------------------------------------------------------------------------------------
		 *	リモートサーバの役割 ---------------------------------------------------------------------------------------------
		 *	１．サーバへの接続(ワールドデータロード)
		 *	２．データのコミット(反映がある場合 ※たとえば、ボタンを押した通知を投げるなど)
		 *	３．データの同期(サーバからワールドデータすべてロード)
		 *	----------------------------------------------------------------------------------------------------------
		 */

	}

	/** @param args { IPAddr(+Port) | FQDN(+Port), WorldName }*/
	public static boolean Init(String[] args) {

		//Loggerの作成
		Logger logger = ServerPropertyZone.logger = Logger.getLogger("ServerApp " + ServerPropertyZone.APPLICATION_VERSION);
		logger.setUseParentHandlers(false);	//親に通知しない

		//コンソールハンドラの作成＆設定
		ConsoleHandler handler = new ConsoleHandler();
		handler.setFormatter(new CustomLogFormatter());
		handler.setLevel(Level.ALL);
		logger.setLevel(Level.ALL);
		logger.addHandler(handler);

		logger.info("------------ SERVER START ------------");
		if(args.length < 2) {
			logger.info("引数が足りないため起動に失敗しました。"); return false;
		} else {
			//引数から必要データの加工
			String serverName; int port;
			{
				String[] splitWord = args[0].split(" ");
				if(splitWord.length == 1) {	//ポート指定がない
					port = 12525;
				} else {
					try {	//ポート番号の取得
						port = Integer.parseInt(splitWord[1]);
					} catch (NumberFormatException e) {	//失敗時は、初期値へ
						port = 12525;
					}
				}
				serverName = splitWord[0];
			}

			{	//接続用意
				if(serverName.replace(".", "-").equals("127-0-0-1")) {	//ループバックアドレスの場合
					//サーバの設立
					try {
						ServerPropertyZone.serverSocket = new ServerSocket(port);
						ServerPropertyZone.threadServer = new ServerThread(args[1]);
						ServerPropertyZone.threadServer.Start();
						ServerPropertyZone.threadRemoteServer = null;
						logger.info(String.format("サーバの設立に成功しました。Port(%d)", port));
					} catch (IOException e) {
						logger.info(String.format("サーバの設立に失敗しました。Port(%d)", port)); return false;
					}
				} else {	//それ以外
					//リモートサーバの設立
					try {
						ServerPropertyZone.serverSocket = new ServerSocket(port);
						ServerPropertyZone.remoteSocket = new Socket(serverName, port);
						ServerPropertyZone.threadRemoteServer = new RemoteServerThread(args[1]);
						ServerPropertyZone.threadRemoteServer.Start();
						ServerPropertyZone.threadServer = null;
						logger.info(String.format("リモートサーバの設立に失敗しました。ServerName(%s) Port(%d)", serverName, port));
					} catch (IOException e) {
						logger.info(String.format("リモートサーバの設立に失敗しました。ServerName(%s) Port(%d)", serverName, port)); return false;
					}
				}
			}

			return true;
		}
	}

	public static void Stop() {
		if(ServerPropertyZone.threadServer != null) { ServerPropertyZone.threadServer.Stop(); }
		if(ServerPropertyZone.threadRemoteServer != null) { ServerPropertyZone.threadRemoteServer.Stop(); }
	}
}
