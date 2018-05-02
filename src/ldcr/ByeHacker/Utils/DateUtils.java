package ldcr.ByeHacker.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    public static String formatDate(final long time) {
	final SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	return formater.format(time);
    }
    public static String formatDate(final Date time) {
	final SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	return formater.format(time);
    }
    public static String formatDateWhitoutTime(final Date time) {
	final SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
	return formater.format(time);
    }
}
