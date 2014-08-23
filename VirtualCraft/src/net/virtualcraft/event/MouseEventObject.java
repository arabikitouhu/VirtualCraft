package net.virtualcraft.event;

public class MouseEventObject extends EventObject {

	/** マウスボタンID(Left) */		public static final int MOUSE_BUTTON_ID_LEFT = 0;
	/** マウスボタンID(Right) */	public static final int MOUSE_BUTTON_ID_RIGHT = 1;

	/** マウス状態(なし) */			public static final int MOUSE_STATE_NONE = 0;
	/** マウス状態(押した) */		public static final int MOUSE_STATE_PRESS = 1;
	/** マウス状態(離した) */		public static final int MOUSE_STATE_RELEASE = 2;
	/** マウス状態(クリック) */		public static final int MOUSE_STATE_CLECK = 3;


	public int intX, intY, intLeftState, intRightState;

	public MouseEventObject(int x, int y, int leftState, int rightState) {
		intX = x;
		intY = y;
		intLeftState = leftState;
		intRightState = rightState;
	}
}
