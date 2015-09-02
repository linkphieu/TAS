package hanelsoft.vn.timeattendance.linkstech.helper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ImageHelper {

    public static byte[] resizeImage(byte[] input) {
        int percent = 99;
        byte[] returnByte = input;
        if (returnByte.length>1024000){
            percent = 10;
        }
        Bitmap original = BitmapFactory.decodeByteArray(input, 0, input.length);
        while (returnByte.length > 102400) {
            Bitmap resized = Bitmap.createScaledBitmap(original, original.getWidth() * percent / 100, original.getHeight() * percent / 100, true);
            ByteArrayOutputStream blob = new ByteArrayOutputStream();
            resized.compress(Bitmap.CompressFormat.JPEG, percent, blob);
            returnByte = blob.toByteArray();
            percent --;
        }
        return returnByte;
    }

    public static Bitmap scaleImage(Bitmap image, int minWidth) {
        int percent = 100;
        Bitmap scaled = image;
        while (scaled.getWidth() > minWidth) {
            scaled = Bitmap.createScaledBitmap(image, image.getWidth() * percent / 100, image.getHeight() * percent / 100, true);
            percent--;
        }
        return scaled;
    }

    public static byte[] decodeImageFile(File f) {
        try {
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);
            final int REQUIRED_WIDTH = 320;
            final int REQUIRED_HIGHT = 240;
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_WIDTH && o.outHeight / scale / 2 >= REQUIRED_HIGHT)
                scale *= 2;
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            Bitmap resized = BitmapFactory.decodeStream(new FileInputStream(f), null, o2);

            ByteArrayOutputStream blob = new ByteArrayOutputStream();
            resized.compress(Bitmap.CompressFormat.JPEG, 100, blob);
            byte[] returnByte = blob.toByteArray();
            return returnByte;
        } catch (FileNotFoundException e) {
        }
        return null;
    }

    public static Bitmap rolateImage(String filePath) throws IOException {
        BitmapFactory.Options bounds = new BitmapFactory.Options();
        bounds.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, bounds);

        BitmapFactory.Options opts = new BitmapFactory.Options();
        Bitmap bm = BitmapFactory.decodeFile(filePath, opts);
        ExifInterface exif = new ExifInterface(filePath);
        String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
        int orientation = orientString != null ? Integer.parseInt(orientString) : ExifInterface.ORIENTATION_NORMAL;

        int rotationAngle = 0;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;

        Matrix matrix = new Matrix();
        matrix.setRotate(rotationAngle, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
        return Bitmap.createBitmap(bm, 0, 0, bounds.outWidth, bounds.outHeight, matrix, true);
    }
}
