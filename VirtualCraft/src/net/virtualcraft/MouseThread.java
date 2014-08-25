package net.virtualcraft;

import net.virtualcraft.control.Window;
import net.virtualcraft.event.MouseEventObject;
import net.virtualcraft.util.FPSWatcher;

import org.lwjgl.input.Mouse;

public class MouseThread implements Runnable {

	private Window parentControl;			//イベントを実行するコントロール(親)

	private Thread thread;
	private boolean isRunning;

	private FPSWatcher watcher;

	private boolean[] leftState, rightState;

	public MouseThread(Window parent) {
		parentControl = parent;
		thread = new Thread(this, "MouseThread");
		watcher = new FPSWatcher(60, "MouseThread", PublicPropertyZone.loggerClient);
	}

	@Override
	public void run() {

		while(isRunning) {
			watcher.PreTick();
			leftState = new boolean[2];
			rightState = new boolean[2];
			while (isRunning && Mouse.next()){
				if (Mouse.getEventButtonState()) {		//押した場合
					if 		(Mouse.getEventButton() == MouseEventObject.MOUSE_BUTTON_ID_LEFT) { leftState[0] = true; }
					else if	(Mouse.getEventButton() == MouseEventObject.MOUSE_BUTTON_ID_RIGHT) { rightState[0] = true; }
				} else {								//離した場合
					if 		(Mouse.getEventButton() == MouseEventObject.MOUSE_BUTTON_ID_LEFT) { leftState[1] = true; }
					else if	(Mouse.getEventButton() == MouseEventObject.MOUSE_BUTTON_ID_RIGHT) { rightState[1] = true; }
				}
			}

			{
				int left =	(leftState[0] ? MouseEventObject.MOUSE_STATE_PRESS : MouseEventObject.MOUSE_STATE_NONE) +
							(leftState[1] ? MouseEventObject.MOUSE_STATE_RELEASE : MouseEventObject.MOUSE_STATE_NONE);
				int right =	(rightState[0] ? MouseEventObject.MOUSE_STATE_PRESS : MouseEventObject.MOUSE_STATE_NONE) +
							(rightState[1] ? MouseEventObject.MOUSE_STATE_RELEASE : MouseEventObject.MOUSE_STATE_NONE);
				MouseEventObject eventObj = new MouseEventObject(Mouse.getX(), Mouse.getY(), left, right);
				parentControl.setMouseEventObject(eventObj);
			}

			long sleepTime = watcher.TickProgress();
			if(sleepTime > 0) {
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			watcher.TickSleep();
			watcher.Update();
		}
	}


	public void Start() { isRunning = true; thread.start(); }

	public void Stop() { isRunning = false; }
}
