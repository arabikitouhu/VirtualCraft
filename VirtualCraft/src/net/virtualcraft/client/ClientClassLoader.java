package net.virtualcraft.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import net.virtualcraft.PublicPropertyZone;

public class ClientClassLoader {

	String[] strHitFiles;

	private Logger logger;

	private String strRootPath;

	public ClientClassLoader() {

		logger = Logger.getLogger("Client ClassLoader");
		logger.setLevel(Level.INFO);
		logger.setParent(PublicPropertyZone.logger);

		ArrayList<String> names = new ArrayList<String>();

		File root = new File(PublicPropertyZone.strRootPath + "/mod/client");
		strRootPath = root.getAbsolutePath();
		File[] files = root.listFiles();	//ファイル一覧を取得
		logger.info("Search Start... (" + strRootPath + ")");
		if(files == null) {
			logger.info("Total number of files. 0"); return;
		}
		logger.info(String.format("Total number of files. %d", files.length));
		for(File file : files) {
			if(file.getName().endsWith(".zip")) {	//「*.zip」拡張子の確認
				names.add(file.getName());
				logger.info(String.format("＋ I found a Zip file. %s", file.getName()));
			}
		}
		logger.info(String.format("Number you find is %d.", names.size()));

		strHitFiles = names.toArray(new String[0]);
	}


	public void onLoad() {
		logger.info("------------ ModLoader(Client) START ------------");

		PublicPropertyZone.mapModEP.clear();

		// 検索処理
		for(String filename : strHitFiles) {
			logger.info(String.format("ZipFile open stream. %s\\%s", strRootPath, filename));
			try {
				//「*.class」を全検索
				ArrayList<String> names = new ArrayList<String>();
				ZipInputStream stream = new ZipInputStream(new FileInputStream(strRootPath + "\\" + filename), StandardCharsets.UTF_8);
				for (ZipEntry entry = stream.getNextEntry(); entry != null; entry = stream.getNextEntry()) {
					if(entry.isDirectory()) continue;	//ディレクトリは飛ばす

					if(entry.getName().endsWith(".class")) {	//「*.class」のフィルタ
//						String uri = entry.getName().replace("/", ".");
//						uri = uri.substring(0, uri.length() - 6);
//						names.add(uri);
						names.add(entry.getName().substring(0, entry.getName().length() - 6));
					}
				}
				stream.close();

				// エントリポイントの検索
				File file = new File(strRootPath + File.separator + filename);
				URL url = file.getCanonicalFile().toURI().toURL();
				URLClassLoader loader = new URLClassLoader( new URL[] { url });

				for(String name : names.toArray(new String[0])) {
					try {
						Class<?> clazz = loader.loadClass(name);

						for(Class<?> iface : clazz.getInterfaces()) {
							if (iface == IModEntryPoint.class) {
								IModEntryPoint modEP = (IModEntryPoint)clazz.newInstance();
								String key = modEP.getModName();
								if(PublicPropertyZone.mapModEP.containsKey(key)) {
									logger.info(String.format("[ERROR] %sは、登録済みです。", key));
									break;
								} else {
									PublicPropertyZone.mapModEP.put(key, modEP);
									logger.info(String.format("[ERROR] %sを、登録しました。", key));
								}
							}
						}
					} catch (ClassNotFoundException e) {
						logger.info(String.format("[ERROR] ClassNotFoundException (%s)", name));
					} catch (InstantiationException e) {
						logger.info(String.format("[ERROR] InstantiationException (%s)", name));
					} catch (IllegalAccessException e) {
						logger.info(String.format("[ERROR] IllegalAccessException (%s)", name));
					}
				}
				loader.close();

//				ZipInputStream stream = new ZipInputStream(new FileInputStream(strRootPath + "\\" + filename), StandardCharsets.UTF_8);
//
//				for (ZipEntry entry = stream.getNextEntry(); entry != null; entry = stream.getNextEntry()) {
//					if(entry.isDirectory()) continue;	//ディレクトリは飛ばす
//
//					if(entry.getName().endsWith(".class")) {	//「*.class」のフィルタ
//						String uri = entry.getName().replace("/", ".");
//
//						try {
////							Class clazz = this.loadClass(uri.substring(0, uri.length() - 6));
////							Class clazz = this.loadClass(uri);
//							Class clazz = this.loadClass(entry.getName().substring(0, uri.length() - 6));
//
//							for(Class<?> iface : clazz.getInterfaces()) {
//								if (iface == IModEntryPoint.class) {
//									IModEntryPoint modEP = (IModEntryPoint)clazz.newInstance();
//									String key = modEP.getModName();
//									if(PublicPropertyZone.mapModEP.containsKey(key)) {
//										logger.info(String.format("[ERROR] %sは、登録済みです。", key));
//										break;
//									} else {
//										PublicPropertyZone.mapModEP.put(key, modEP);
//										logger.info(String.format("[ERROR] %sを、登録しました。", key));
//									}
//								}
//							}
//						} catch (ClassNotFoundException e) {
//							logger.info(String.format("[ERROR] ClassNotFoundException (%s)", uri));
//						} catch (InstantiationException e) {
//							logger.info(String.format("[ERROR] InstantiationException (%s)", entry.getName()));
//						} catch (IllegalAccessException e) {
//							logger.info(String.format("[ERROR] IllegalAccessException (%s)", entry.getName()));
//						}
//					}
//				}
//				stream.close();
				logger.info(String.format("ZipFile close stream. %s\\%s", strRootPath, filename));
			} catch (IOException e) {
				logger.info(String.format("[ERROR]ZipFile open stream. %s\\%s", strRootPath, filename));
			}
		}

		int startPoint = 65536;
		// 読込処理(開始位置の設定＆初期化(前処理))
		for(Entry<String, IModEntryPoint> entry : PublicPropertyZone.mapModEP.entrySet()) {
			IModEntryPoint ep = entry.getValue();
			ep.setItemIDStartPosition(entry.getKey().equals("VirtualCraft StarterSet") ? 0 : startPoint);
			startPoint += 65536;
			ep.preInitialize();
		}
		// 読込処理(初期化)
		for(Entry<String, IModEntryPoint> entry : PublicPropertyZone.mapModEP.entrySet()) {
			IModEntryPoint ep = entry.getValue();
			ep.Initialize();
		}
		// 読込処理(後初期化)
		for(Entry<String, IModEntryPoint> entry : PublicPropertyZone.mapModEP.entrySet()) {
			IModEntryPoint ep = entry.getValue();
			ep.postInitialize();
		}


		logger.info("------------ ModLoader(Client) END   ------------");
	}
}