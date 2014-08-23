package net.virtualcraft.util;

import java.util.logging.Level;
import java.util.logging.Logger;

import net.virtualcraft.PublicPropertyZone;

public class FPSWatcher {

	private int intFrame;

	private int intFixedFrame;

	private float floatFps;

	private long longTotalProgressTime, longTotalSleepTime;

	private float floatAveProgressTime, floatAveSleepTime;

	private Logger logger;

	private long longBeforeTime;

	public FPSWatcher(int fixedFrame, String name) {
		logger = Logger.getLogger(name + " FPSWather");
		logger.setLevel(Level.INFO);
		logger.setParent(PublicPropertyZone.logger);


		intFixedFrame = fixedFrame;

		floatFps = 1000.000f / fixedFrame;

		intFrame = 0;

		longTotalProgressTime = 0;
		longTotalSleepTime = 0;
		floatAveProgressTime = 0.0f;
		floatAveSleepTime = 0.0f;
	}

	public void PreTick() {
		longBeforeTime = System.currentTimeMillis();
	}

	public long TickProgress() {
		long nowTime = System.currentTimeMillis();

		longTotalProgressTime += (nowTime - longBeforeTime);

		floatAveProgressTime = (float)longTotalProgressTime / (float)intFrame;

		longBeforeTime = System.currentTimeMillis();

		return (int)(floatFps - (nowTime - longBeforeTime));
	}
	public void TickSleep() {
		long nowTime = System.currentTimeMillis();

		longTotalSleepTime += (nowTime - longBeforeTime);

		floatAveSleepTime = (float)longTotalSleepTime / (float)intFrame;
	}
	public void Update() {
		if(++intFrame == intFixedFrame) {
			intFrame = 0;
			longTotalProgressTime = 0;
			longTotalSleepTime = 0;
			float aveFPS = 1000.000f / (floatAveProgressTime + floatAveSleepTime);
			logger.info(String.format("Interval. AveFPS(%.2f) AveTime(%.2f | %.2f)", aveFPS, floatAveProgressTime, floatAveSleepTime));
		}
	}
}
