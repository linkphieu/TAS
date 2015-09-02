package hanelsoft.vn.timeattendance.common.servicenetwork.ultis;

import android.content.Context;
import android.content.res.Resources;

public class StringUtils {

	/**
	 * check string is null or contains only character blank
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isBlank(String str) {
		return (str == null || str.trim().length() == 0);
	}

	public static boolean isNotBlank(String str) {
		return !isBlank(str);
	}

	public static String getResString(String resCode, Context context) {
		String resStr = resCode;
		Resources res = context.getResources();

		if (res != null) {
			int resId = res.getIdentifier(resCode, "string",
					"com.sofrecom.megatools");
			if (resId != 0) {
				resStr = res.getString(resId);
			}
		}

		return resStr;
	}
}
