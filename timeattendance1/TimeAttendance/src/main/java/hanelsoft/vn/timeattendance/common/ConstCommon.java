package hanelsoft.vn.timeattendance.common;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.SystemClock;

import com.google.android.gms.maps.model.LatLng;

import java.io.File;

import hanelsoft.vn.timeattendance.model.DAO.daoEmp;

public class ConstCommon {
	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;
	public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "timeAttendance";
	public static final String TABLE_NAME = "clock";

	public static final long Time = SystemClock.uptimeMillis();

	public static final String KEY_ID = "id";
	public static final String KEY_AUTO_TIME = "auto_time";
	public static final String KEY_EMPLOYEE_ID = "employeeID";
	public static final String KEY_LAT = "lat";
	public static final String KEY_LNG = "lng";
	public static final String KEY_IMAGE = "image";
	public static final String KEY_TIME = "time";
	public static final String KEY_STATUS = "status";
	public static final String KEY_ACTION = "action";
	// public static final String URL_SERVER = "http://27.118.21.3:9999";
	// public static final String URL_SERVER = "http://a21.sg:81/TAS";
	public static final String URL_SERVER = "http://118.189.60.23:84/TAS";
	// public static String URL_SERVER = "";
	public static final String NAMESPACE = "http://tempuri.org/";
	public static final String URL = URL_SERVER + "/Service1.asmx?WSDL";
	public static final String SOAP_ACTION_GET_DATA = "http://tempuri.org/GetData";
	public static final String SOAP_ACTION_UPLOAD = "http://tempuri.org/Base64StringToImage";
	public static final String METHOD_NAME_GET_DATA = "GetData";
	public static final String METHOD_NAME_UPLOAD = "Base64StringToImage";
	public static final String METHOD_NAME_CHECK_LOGIN = "CheckLogin";
	public static final String METHOD_NAME_GETDATA_EMPIMAGE = "GetDataImage";
	public static final String METHOD_NAME_ADD_EMPLOYEE = "AddEmployee";
	public static final String SOAP_ACTION_CHECK_LUNCH = "http://tempuri.org/SettingForLunch";
	public static final String METHOD_CHECK_LUNCH = "SettingForLunch";
	public static final String SOAP_ACTION_CHECK_EMPID = "http://tempuri.org/CheckEmpID";
	public static final String SOAP_ACTION_CHECK_LOGIN = "http://tempuri.org/CheckLogin";
	public static final String SOAP_ACTION_ADD_EMPLOYEE = "http://tempuri.org/AddEmployee";
	public static final String SOAP_ACTION_GETDATA_EMPIMAGE = "http://tempuri.org/GetDataImage";
	public static final String METHOD_NAME_CHECK_EMPID = "CheckEmpID";
	public static final String SOAP_ACTION_SYNCDATA = "http://tempuri.org/SyncData";
	public static final String METHOD_NAME_SYNCDATA = "SyncData";
	public static final String TIME_SERVER = "time-a.nist.gov";
	public static long networkTS = 0;
	public static int CountTotalImage = 0;
	public static String pathImage = "";
	public static String pin1 = "";
	public static String pin2 = "";
	public static String pin3 = "";
	public static String pin4 = "";
	public static String EmID = "";
	public static double latitude = 0;
	public static double longitude = 0;
	public static String ProjectID = "";
	public static String ProjectLocationID = "";
	public static String nameEmp = "";
	public static int action;
	public static String TimeClock = "";
	public static LatLng latLng;
	public static long returnTime = 0;
	public static boolean flag = true;
	public static File mCascadeFile;
	public static File pictureFile = null;
	public static String pathPicture = "";
	public static String pathPictureToUpload = "";
	public static Intent intent;
	public static String recogEmpID = "";

	public static void resetValue() {
		ConstCommon.pin1 = "";
		ConstCommon.pin2 = "";
		ConstCommon.pin3 = "";
		ConstCommon.pin4 = "";
		ConstCommon.EmID = "";

	}

	public static daoEmp daoEmpWhenPinCode;
	public static String STR_CLOCK_IN = "CLOCKIN";
	public static String STR_CLOCK_OUT = "CLOCKOUT";
	public static String PROJECT_SKIP = "FALSE";
	public static boolean PROJECT_IS_SKIP = false;

	public static String KEY_SHAREPREFERENCE = "LuongVD_SHAREPREFERENCE";
	public static String LuongVD_SHAREPREFERENCE_CLOCK_IN_PROJECT = "LuongVD_SHAREPREFERENCE_CLOCK_IN_PROJECT";
	public static String LuongVD_SHAREPREFERENCE_CLOCK_IN_PROJECT_LAT = "LuongVD_SHAREPREFERENCE_CLOCK_IN_PROJECT_LAT";
	public static String LuongVD_SHAREPREFERENCE_CLOCK_IN_PROJECT_LON = "LuongVD_SHAREPREFERENCE_CLOCK_IN_PROJECT_LON";
	public static Bitmap bitMapImageWhenSkip = null;
}
