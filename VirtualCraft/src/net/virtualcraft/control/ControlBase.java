package net.virtualcraft.control;

import java.util.HashMap;
import java.util.Map.Entry;

import net.virtualcraft.event.MouseEventObject;

public abstract class ControlBase {

	protected int intX, intY, intWidth, intHeight;

	/** 衝突したかの判定 */
	public boolean isCollided(int x, int y) {
		if(x < intX && x > intWidth) return false;
		if(y < intY && x > intHeight) return false;
		return true;
	}

	/** マウスイベント用オブジェクト配置
	 * @return 処理を停止するかの判定値 */
	public abstract boolean setMouseEventObject(MouseEventObject obj);



	protected String strID;		//識別用ID

	/** 識別用ID(*.xmlで指定可能)の取得 */
	public String getID() { return strID; }

	protected HashMap<String, ControlBase> mapContains = new HashMap<String, ControlBase>();




	protected boolean hasFocus;	//フォーカスをもっているかの判定値

	/** フォーカスを受け取れるか。 */
	public abstract boolean canFocus();

	/** フォーカスがあるか。 */
	public boolean Focused() { return canFocus() ? hasFocus : false; }

	/** フォーカスを設定します。 */
	public void Focus() { if(canFocus()) { hasFocus = true; } }

	/** フォーカスを解除します。 */
	public void LostFocus() { if(canFocus()) { hasFocus = false; } }

	/** 自身または、子コントロールにフォーカスがあるか。 */
	public boolean ContainsFocus() {
		if(Focused()) return true;		//自身がフォーカスをもっている場合

		if(mapContains.isEmpty()) return false;	//子コントロールが存在しない場合

		//子コントロールが持っているかの確認
		for(Entry<String, ControlBase> entry : mapContains.entrySet()) {
			ControlBase control = entry.getValue();
			if(control.ContainsFocus()) return true;	//子コントロールも同様にContainsFocus()を実行。
		}

		return false;	//該当なしの場合
	}
}
