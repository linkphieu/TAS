package hanelsoft.vn.timeattendance.common.servicenetwork.ultis;

import hanelsoft.vn.timeattendance.common.ConstCommon;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.net.Uri;
import android.os.Environment;
import android.util.Log;

public class FileUtil {
	public static String getFormatedFileSize(File f) {

		if (f != null && f.exists()) {
			return formatSize(f.length(), 1);
		}
		return "";
	}

	public static String formatSize(long longSize, int decimalPos) {
		NumberFormat fmt = NumberFormat.getNumberInstance();
		if (decimalPos >= 0) {
			fmt.setMaximumFractionDigits(decimalPos);
		}
		final double size = longSize;
		double val = size / (1024 * 1024);
		if (val > 1) {
			return fmt.format(val).concat(" MB");
		}
		val = size / 1024;
		if (val > 1) {
			return fmt.format(val).concat(" KB");
		}
		return fmt.format(size).concat(" bytes");
	}

	/**
	 * get file name from full file path
	 * 
	 * @param fullpath
	 * @param containExt
	 * @return
	 */
	public static String getFileName(String fullpath, boolean containExt) {
		String ret = fullpath.substring(
				fullpath.lastIndexOf(File.separator) + 1, fullpath.length());
		if (!containExt) {
			ret = ret.substring(0, ret.lastIndexOf("."));
		}
		return ret;

	}

	public static String getFormatedDuration(long duration) {
		duration /= 1000;// duration in second
		final int SECOND = 60;
		final int MIN = SECOND * 60;

		int minutes = (int) (duration / SECOND % SECOND);
		int hours = (int) (duration / MIN % (MIN));

		int seconds = (int) duration - hours * MIN - minutes * SECOND;

		return String.format("%02d:%02d:%02d", hours, minutes, seconds);

	}

	public static synchronized byte[] getChunk(File file, int offset,
			int chunkSize) throws IOException {
		InputStream inputStream = new FileInputStream(file);
		inputStream.skip(offset);
		long bytesLeft = file.length() - offset;

		int bytesToRead = bytesLeft < chunkSize ? (int) bytesLeft : chunkSize;

		byte[] buffer = new byte[bytesToRead];

		offset += inputStream.read(buffer, 0, bytesToRead);

		if (bytesToRead <= chunkSize) {
			inputStream.close();
		}

		return buffer;
	}

	public static int getTypeList(String type) {
		Map<String, Integer> mMap = new HashMap<String, Integer>();
		mMap.put("jpg", new Integer(1));
		mMap.put("gif", new Integer(1));
		mMap.put("bmp", new Integer(1));
		mMap.put("png", new Integer(1));
		mMap.put("jpeg", new Integer(1));
		mMap.put("jpe", new Integer(1));
		mMap.put("tif", new Integer(1));
		mMap.put("mp4", new Integer(2));
		mMap.put("3gp", new Integer(2));
		mMap.put("3g2", new Integer(2));
		mMap.put("3gp2", new Integer(2));
		mMap.put("mov", new Integer(2));
		mMap.put("mgp", new Integer(2));
		mMap.put("avi", new Integer(2));
		mMap.put("doc", new Integer(3));
		mMap.put("txt", new Integer(3));
		mMap.put("docx", new Integer(3));
		mMap.put("rtf", new Integer(3));
		mMap.put("log", new Integer(3));
		mMap.put("docx", new Integer(3));

		Integer ret = (Integer) mMap.get(type);
		if (ret == null)
			return 4;
		else
			return ret.intValue();

	}

	public static void createDirIfNeed(String fullPath) {
		File f = new File(fullPath);
		if (!f.exists()) {
			f.mkdirs();
		}
	}

	public static Uri getOutputMediaFileUri(int type) {
		return Uri.fromFile(getOutputMediaFile(type));
	}

	public static File getOutputMediaFile(int type) {

		File mediaStorageDir = new File(
				Environment.getExternalStorageDirectory(), "Capture");
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.d("MyCameraApp", "failed to create directory");
				return null;
			}
		}

		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		File mediaFile;
		if (type == ConstCommon.MEDIA_TYPE_IMAGE) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator
					+ "IMG_" + timeStamp + ".jpg");
		} else if (type == ConstCommon.MEDIA_TYPE_VIDEO) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator
					+ "VID_" + timeStamp + ".mp4");
		} else {
			return null;
		}

		return mediaFile;
	}
}
