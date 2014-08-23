package net.virtualcraft.event;

public interface MouseEventListener {

	/** マウスフォーカスが外れたときのイベント */
	public void onMouseLostFocus(MouseEventObject obj);

	/** マウスフォーカスが与えられたときのイベント */
	public void onMouseFocus(MouseEventObject obj);

	/** マウスが押されたときのイベント */
	public void onMouseDown(MouseEventObject obj);

	/** クリックしたときのイベント */
	public void onMouseClick(MouseEventObject obj);

	/** マウスが押されたときのイベント */
	public void onMouseUp(MouseEventObject obj);
}
