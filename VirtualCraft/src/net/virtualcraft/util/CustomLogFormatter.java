package net.virtualcraft.util;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class CustomLogFormatter extends Formatter {

	@Override
	public String format(LogRecord record) {
		//時間の取得
		long millis = record.getMillis();
		String time = String.format("%tD %<tT.%<tL", millis);

		//ログ名の取得
		String name = record.getLoggerName();

		//ログLevelの取得
		String level = record.getLevel().getLocalizedName();

		//メッセージの取得
		String message = formatMessage(record);

		return String.format("%s (%s) %s： %s\n", time, name, level, message);
	}

}
