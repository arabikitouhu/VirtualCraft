package net.virtualcraft.control;

import java.util.Map.Entry;

import net.virtualcraft.event.MouseEventListener;
import net.virtualcraft.event.MouseEventObject;
import net.virtualcraft.util.BitHelper;

public class Window extends ControlBase implements MouseEventListener {

	protected boolean hasMouseFocus;

	public Window(int x, int y, int width, int height) {
		hasMouseFocus = false;

		this.intX = x;
		this.intY = y;
		this.intWidth = width;
		this.intHeight = height;
	}

	@Override
	public void onMouseLostFocus(MouseEventObject obj) {
		System.out.println("Window.onMouseLostFocus()");
	}

	@Override
	public void onMouseFocus(MouseEventObject obj) {
		System.out.println("Window.onMouseFocus()");
	}

	@Override
	public void onMouseDown(MouseEventObject obj) {
		System.out.println("Window.onMouseDown()");
	}

	@Override
	public void onMouseClick(MouseEventObject obj) {
		System.out.println("Window.onMouseClick()");
	}

	@Override
	public void onMouseUp(MouseEventObject obj) {
		System.out.println("Window.onMouseUp()");
	}

	@Override
	public boolean canFocus() { return false; }

	@Override
	public boolean setMouseEventObject(MouseEventObject obj) {
		for(Entry<String, ControlBase> entry : this.mapContains.entrySet()) {
			if(entry.getValue().setMouseEventObject(obj)) {
				return true;
			}
		}

		boolean collided = isCollided(obj.intX, obj.intY);		//マウスが範囲内にあるかの判定
		if(hasMouseFocus && !collided) {	//Enter→Leave
			onMouseLostFocus(obj);
		} else if(!hasMouseFocus && collided) {	//Leave→Enter
			onMouseFocus(obj);
		}
		hasMouseFocus = collided;

		if(hasMouseFocus) {
			//クリックイベント
			if(BitHelper.getBit(obj.intLeftState, MouseEventObject.MOUSE_STATE_PRESS) || BitHelper.getBit(obj.intRightState, MouseEventObject.MOUSE_STATE_PRESS)) {
				onMouseDown(obj);
			}
			if(BitHelper.getBit(obj.intLeftState, MouseEventObject.MOUSE_STATE_RELEASE) || BitHelper.getBit(obj.intRightState, MouseEventObject.MOUSE_STATE_RELEASE)) {
				onMouseClick(obj);
				onMouseUp(obj);
			}
		}

		return hasMouseFocus;
	}

}
