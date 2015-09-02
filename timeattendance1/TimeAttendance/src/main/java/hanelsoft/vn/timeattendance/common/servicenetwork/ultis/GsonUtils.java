package hanelsoft.vn.timeattendance.common.servicenetwork.ultis;

import hanelsoft.vn.timeattendance.common.servicenetwork.model.ServerError;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.Gson;

public class GsonUtils {
	@SuppressWarnings("rawtypes")
	public static ArrayList convertToList(String str, Type listType) {
		Gson gson = new Gson();
		return gson.fromJson(str, listType);
	}

	public static Object convertToObject(String str, Type type) {
		Gson gson = new Gson();
		if (str.trim().startsWith("{\"e\":{")) {
			// Error
			String errorObj = str.substring(5, str.length() - 2).replace(
					"\"m\"", "\"message\"");
			return gson.fromJson(errorObj, ServerError.class);
		}
		return gson.fromJson(str, type);
	}

	public static String convertToString(Object obj, Type type) {
		Gson gson = new Gson();
		return gson.toJson(obj, type);
	}
}
