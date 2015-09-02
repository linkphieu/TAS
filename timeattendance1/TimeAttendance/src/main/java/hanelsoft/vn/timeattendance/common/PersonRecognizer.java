package hanelsoft.vn.timeattendance.common;

import hanelsoft.vn.timeattendance.utils.DialogManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import com.googlecode.javacv.cpp.opencv_contrib.FaceRecognizer;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.googlecode.javacv.cpp.opencv_core.MatVector;
import com.googlecode.javacv.cpp.opencv_imgproc;

public class PersonRecognizer {
	private File mCascadeFile;
	public final static int MAXIMG = 10;
	FaceRecognizer faceRecognizer;
	String mPath;
	int count = 0;
	Context context;
	static final int WIDTH = 128;
	static final int HEIGHT = 128;;
	private int mProb = 999;

	public PersonRecognizer(String path, Context context) {
		faceRecognizer = com.googlecode.javacv.cpp.opencv_contrib
				.createLBPHFaceRecognizer(2, 8, 8, 8, 200);
		// path=Environment.getExternalStorageDirectory()+"/facerecog/faces/";
		mPath = path;
		this.context = context;

	}

	void changeRecognizer(int nRec) {
		switch (nRec) {
		case 0:
			faceRecognizer = com.googlecode.javacv.cpp.opencv_contrib
					.createLBPHFaceRecognizer(1, 8, 8, 8, 100);
			break;
		case 1:
			faceRecognizer = com.googlecode.javacv.cpp.opencv_contrib
					.createFisherFaceRecognizer();
			break;
		case 2:
			faceRecognizer = com.googlecode.javacv.cpp.opencv_contrib
					.createEigenFaceRecognizer();
			break;
		}
		train();

	}



	public boolean train() {
		try {
			File root = new File(mPath);
			if (root.isDirectory()) {
			} else if (root.isFile()) {
			}
			FilenameFilter pngFilter = new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return name.toLowerCase().endsWith(".jpg");

				};
			};

			File[] imageFiles = root.listFiles(pngFilter);
			MatVector images = null;
			try {
				images = new MatVector(imageFiles.length);
			} catch (Exception e) {
				// TODO: handle exception
			}

			int[] labels = null;
			try {
				labels = new int[imageFiles.length];
			} catch (Exception e) {
				// TODO: handle exception
			}

			int counter = 0;
			int label;

			IplImage img = null;
			IplImage grayImg;

			int i1 = mPath.length();

			for (File image : imageFiles) {
				String p = image.getAbsolutePath();

			
				// Log.i("image", p);
				if (img == null) {
					((Activity) context).runOnUiThread(new Runnable() {
						@Override
						public void run() {
							DialogManager.showErrorMessage("Load Image Error",
									context);
						}
					});
					return false;

				}

				// Log.i("image", p);

				int i2 = p.lastIndexOf("-");
				int i3 = p.lastIndexOf(".");
				int icount = Integer.parseInt(p.substring(i2 + 1, i3));
				if (count < icount)
					count++;

				String description = p.substring(i1, i2);

				counter++;
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}

		return true;
	}


	protected void SaveBmp(Bitmap bmp, String path) {
		FileOutputStream file;
		try {
			file = new FileOutputStream(path, true);
			bmp.compress(Bitmap.CompressFormat.JPEG, 100, file);
			file.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void load() {
		train();

	}

	public int getProb() {
		// TODO Auto-generated method stub
		return mProb;
	}

}
