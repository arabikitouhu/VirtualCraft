package net.virtualcraft.client;

/** Modのエントリーポイント用インタフェース */
public interface IModEntryPoint {

	/** getter(Mod名) */
	public String getModName();

	/** getter(Modバージョン) */
	public String getModVersion();

	/** setter(ID開始位置) */
	public void setIDStartPosition(int start);

	/** 初期化(前処理) */
	public void preInitialize();

	/** 初期化 */
	public void Initialize();

	/** 初期化(後処理) */
	public void postInitialize();

}
