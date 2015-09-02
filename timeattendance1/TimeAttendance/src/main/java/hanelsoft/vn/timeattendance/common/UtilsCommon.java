package hanelsoft.vn.timeattendance.common;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import hanelsoft.vn.timeattendance.linkstech.helper.ImageHelper;

public class UtilsCommon {

	public static Boolean isClickForResult, isCheckedForResult;
	// ProgressDialog
	public static ProgressDialog dialog;
	Context context;
	Activity mActivity;
	static int serverResponseCode = 0;

	public UtilsCommon(Context context) {
		this.context = context;
	}

	public static void startLoading(Context context) {
		if (dialog == null) {
			dialog = ProgressDialog.show(context, "Loading", "Please wait...",
					true);
		}
	}

	public static void stopLoading() {
		if (dialog != null) {
			if (dialog.isShowing()) {
				dialog.dismiss();
				dialog = null;
			}

		}
	}

	public static boolean haveNetworkConnection(Context context) {
		boolean haveConnectedWifi = false;
		boolean haveConnectedMobile = false;

		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] netInfo = cm.getAllNetworkInfo();

		for (NetworkInfo ni : netInfo) {
			if (ni.getTypeName().equalsIgnoreCase("WIFI"))
				if (ni.isConnected())
					haveConnectedWifi = true;
			if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
				if (ni.isConnected())
					haveConnectedMobile = true;
		}
		return haveConnectedWifi || haveConnectedMobile;
	}

	public static String encodeImage(String path) {
		String encodedImage = null;
		try {
			File imagefile = new File(path);
//			FileInputStream fis = null;
//			fis = new FileInputStream(imagefile);
//			Bitmap bm = BitmapFactory.decodeStream(fis);
//			ByteArrayOutputStream baos = new ByteArrayOutputStream();
//			bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
//			byte[] b = baos.toByteArray();
			byte[] b = ImageHelper.decodeImageFile(imagefile);
			encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return encodedImage;
	}

	public static void DownLoadImage(String urlServer, String IDEmp, String path) {
		try {
			int _count = 0;
			File f = new File(path + IDEmp + "-" + _count + ".jpg");
			URL url = new URL(urlServer);
			URLConnection conection = url.openConnection();
			conection.connect();
			InputStream input = new BufferedInputStream(url.openStream(), 8192);
			OutputStream output = new FileOutputStream(f);

			byte data[] = new byte[1024];
			long total = 0;
			int count = 0;
			while ((count = input.read(data)) != -1) {
				total++;
				output.write(data, 0, count);
			}
			_count++;
			output.flush();
			output.close();
			input.close();
		} catch (Exception e) {
			e.printStackTrace();
			// dialog.dismiss();
		}

	}

	public static void DecodeImage(Context context, String base64, String name,
			String path, int i) {
		FileOutputStream fos = null;
		// String mpath = Environment.getExternalStorageDirectory()
		// .getAbsolutePath() + "/test2/";
		// Log.i("MPath:",mpath);
		String mPath = path + name;

		boolean success = (new File(mPath)).mkdirs();
		File f = new File(mPath + "/" + name + "-" + i + ".jpg");
		// File f = new File(mpath + name + "-" + i + ".jpg");
		try {
			// fos = context.openFileOutput(mpath + name + "-" + _count +
			// ".jpg", Context.MODE_PRIVATE);
			fos = new FileOutputStream(f);
			byte[] decodedString = android.util.Base64.decode(base64,
					android.util.Base64.DEFAULT);
			fos.write(decodedString);
			fos.flush();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		} finally {
			if (fos != null) {
				fos = null;
			}
		}
	}

	public static double CalculationByDistance(double latFrom, double lngFrom,
			String latTo, String lngTo) {
		int Radius = 6371;// radius of earth in Km
		double dLat = Math.toRadians(latFrom - Double.parseDouble(latTo));
		double dLon = Math.toRadians(lngFrom - Double.parseDouble(lngTo));
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
				+ Math.cos(Math.toRadians(latFrom))
				* Math.cos(Math.toRadians(Double.parseDouble(latTo)))
				* Math.sin(dLon / 2) * Math.sin(dLon / 2);
		double c = 2 * Math.asin(Math.sqrt(a));
		double valueResult = Radius * c;
		double km = valueResult / 1;
		DecimalFormat newFormat = new DecimalFormat("####");
		int kmInDec = Integer.valueOf(newFormat.format(km));
		double meter = valueResult % 1000;
		int meterInDec = Integer.valueOf(newFormat.format(meter));
		Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
				+ " Meter   " + meterInDec);

		return km;
	}

	@SuppressLint("SimpleDateFormat")
	public static String getCurrentDate() {
		String currentDate = "";
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd/MM/yyyy");
		currentDate = sdf.format(new Date());
		return currentDate;
	}

	public static void setSharePreference(Context context, String keyWord,
			String value) {
		SharedPreferences sharedpreferences = context.getSharedPreferences(
				ConstCommon.KEY_SHAREPREFERENCE, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedpreferences.edit();
		editor.putString(keyWord, value);
		editor.commit();
	}

	public static String getSharePreference(Context context, String keyWord) {
		SharedPreferences sharedpreferences = context.getSharedPreferences(
				ConstCommon.KEY_SHAREPREFERENCE, Context.MODE_PRIVATE);
		return sharedpreferences.getString(keyWord, "");
	}
}
