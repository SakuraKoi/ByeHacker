package ldcr.ByeHacker;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
	public static String formatDate(final long time) {
		final SimpleDateFormat formater = new SimpleDateFormat("yyyy-mm-dd hh-MM-ss");
		return formater.format(time);
	}
	public static String formatDate(final Date time) {
		final SimpleDateFormat formater = new SimpleDateFormat("yyyy-mm-dd hh-MM-ss");
		return formater.format(time);
	}
}
