package net.virtualcraft.util;

public class BitHelper {

	/** ビットが「1」の状態 */	public static final boolean BIT_ON = true;
	/** ビットが「0」の状態 */	public static final boolean BIT_OFF = false;

	/** ビットの状態を確認する。 */
	public static boolean getBit(int source, int offset) { return (source & offset) == offset; }
}
