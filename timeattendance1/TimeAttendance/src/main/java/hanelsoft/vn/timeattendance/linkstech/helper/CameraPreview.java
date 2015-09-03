package hanelsoft.vn.timeattendance.linkstech.helper;

import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import hanelsoft.vn.timeattendance.common.ConstCommon;
import hanelsoft.vn.timeattendance.common.servicenetwork.ultis.FileUtil;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder mSurfaceHolder;
    private Camera mCamera;

    // Constructor that obtains context and camera
    @SuppressWarnings("deprecation")
    public CameraPreview(Context context, Camera camera) {
        super(context);
        this.mCamera = camera;
        this.mSurfaceHolder = this.getHolder();
        this.mSurfaceHolder.addCallback(this);
        this.mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_NORMAL);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        try {
            this.rolateCamera();
            mCamera.setPreviewDisplay(surfaceHolder);
            mCamera.startPreview();
        } catch (Exception e) {
            // left blank for now
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
        // start preview with new settings
        try {
            Camera.Parameters parameters = mCamera.getParameters();
            this.rolateCamera();
            mCamera.setPreviewDisplay(surfaceHolder);
            mCamera.startPreview();
        } catch (Exception e) {
            // intentionally left blank for a test
        }
    }

    public void refreshCamera(Camera camera) {
        if (mSurfaceHolder.getSurface() == null) {
            return;
        }
        try {
            mCamera.stopPreview();
        } catch (Exception e) {
        }
        mCamera = camera;
        try {
            Camera.Parameters parameters = mCamera.getParameters();
            this.rolateCamera();
            mCamera.setPreviewDisplay(mSurfaceHolder);
            mCamera.startPreview();
        } catch (Exception e) {
            Log.d(VIEW_LOG_TAG, "Error starting camera preview: " + e.getMessage());
        }
    }

    public void takePicture(final Context context) {
        PictureCallback callback = new PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                ConstCommon.pictureFile = FileUtil
                        .getOutputMediaFile(ConstCommon.MEDIA_TYPE_IMAGE);
                ConstCommon.pathPicture = ConstCommon.pictureFile.getPath();
                if (ConstCommon.pictureFile == null) {
                    return;
                }
                try {
                    data = ImageHelper.resizeImage(data);
                    FileOutputStream fos = new FileOutputStream(
                            ConstCommon.pictureFile);
                    ConstCommon.intent.putExtra("pathImage",
                            ConstCommon.pathPicture);
                    context.startActivity(ConstCommon.intent);
                    fos.write(data);
                    fos.close();
                } catch (FileNotFoundException e) {
                } catch (IOException e) {
                }
            }
        };

        mCamera.takePicture(null, null, callback);
    }

    public void takePicture() {
        PictureCallback callback = new PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                ConstCommon.pictureFile = null;
                ConstCommon.pictureFile = FileUtil.getOutputMediaFile(ConstCommon.MEDIA_TYPE_IMAGE);
                ConstCommon.pathPictureToUpload = ConstCommon.pictureFile.getPath();
                if (ConstCommon.pictureFile == null) {
                    return;
                }
                try {
                    FileOutputStream fos = new FileOutputStream(ConstCommon.pictureFile);
                    fos.write(ImageHelper.resizeImage(data));
                    fos.close();

                } catch (FileNotFoundException e) {
                } catch (IOException e) {
                }
            }
        };

        mCamera.takePicture(null, null, callback);
    }

    private void rolateCamera() {
        Camera.Parameters parameters = mCamera.getParameters();
        if (this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
            parameters.set("orientation", "portrait");
            mCamera.setDisplayOrientation(90);
            parameters.setRotation(90);
        } else {
            parameters.set("orientation", "landscape");
            mCamera.setDisplayOrientation(0);
            parameters.setRotation(0);
        }
    }
}