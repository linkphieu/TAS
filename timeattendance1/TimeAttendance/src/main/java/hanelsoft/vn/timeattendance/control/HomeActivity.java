package hanelsoft.vn.timeattendance.control;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.a2000.tas.R;

import java.text.SimpleDateFormat;
import java.util.Date;

import hanelsoft.vn.timeattendance.common.ConstCommon;
import hanelsoft.vn.timeattendance.common.UtilsCommon;
import hanelsoft.vn.timeattendance.model.helper.CameraPreview;
import hanelsoft.vn.timeattendance.model.DAO.daoEmpClock;
import hanelsoft.vn.timeattendance.model.entity.EmployeeClockEntity;
import hanelsoft.vn.timeattendance.model.entity.EmployeeEntity;
import hanelsoft.vn.timeattendance.model.entity.ProjectEntity;
import hanelsoft.vn.timeattendance.model.entity.TimeSyncEntity;
import hanelsoft.vn.timeattendance.service.TimerService;

@SuppressWarnings("deprecation")
public class HomeActivity extends Activity {

    Intent serviceIntent;
    Button btnClockIn, btnClockOut;
    Button imgMenu;
    ImageButton btnSwitchCamera;
    Boolean isSelectCameraFront;
    TextView txtCurrentDate, txtEmpID, txtMessage;
    FrameLayout preview;
    ProjectEntity entity;
    private Camera mCamera;
    private CameraPreview mCameraPreview;
    String mPath = "";
    TimeSyncEntity timesyncEntity;
    ProjectEntity projectEntity;
    EmployeeEntity employeeEntity;
    EmployeeClockEntity employeeClockEntity;
    //Trungvt
    private TextView lastActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCamera = getCameraInstance();
        mCameraPreview = new CameraPreview(this, mCamera);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        initDatabase();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        isSelectCameraFront = false;
        btnSwitchCamera = (ImageButton) findViewById(R.id.btnSwitchCamera);
        btnClockIn = (Button) findViewById(R.id.btnClockIn);
        btnClockOut = (Button) findViewById(R.id.btnClockOut);
        txtCurrentDate = (TextView) findViewById(R.id.currentDate);
        imgMenu = (Button) findViewById(R.id.menuSkip);
        txtEmpID = (TextView) findViewById(R.id.employeeID);
        txtMessage = (TextView) findViewById(R.id.textView1);
        //Trungvt
        this.lastActivity = (TextView) this.findViewById(R.id.last_activity);
        daoEmpClock _daoEmpClock = employeeClockEntity.getStatusByID(ConstCommon.daoEmpWhenPinCode.getID());
        if(_daoEmpClock.getStatusClock()!=null) {
            this.lastActivity.setText(_daoEmpClock.getDateClock() + " | " + _daoEmpClock.getProjectName() + " | " + _daoEmpClock.getStatusClock());
        }else {
            this.lastActivity.setText("N/A");
        }
        // set Current Date
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMMM dd");
        String currentDate = sdf.format(new Date());
        txtCurrentDate.setText(currentDate);
        preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mCameraPreview);
        // Click Button
        btnClockIn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                daoEmpClock _daoEmpClock = new daoEmpClock();
                _daoEmpClock = employeeClockEntity.getStatusByID(ConstCommon.daoEmpWhenPinCode.getID());
                if (_daoEmpClock.getStatusClock() != null && _daoEmpClock.getStatusClock().length() > 0) {
//					if (_daoEmpClock.getStatusClock().toUpperCase().equals(ConstCommon.STR_CLOCK_IN)) {
//						String message = "You must to Clock out project: "
//								+ _daoEmpClock.getProjectName() + " ("
//								+ _daoEmpClock.getProjectLat() + ", "
//								+ _daoEmpClock.getProjectLon()
//								+ ") before Clock in";
//
//						Toast.makeText(getBaseContext(), message,
//								Toast.LENGTH_LONG).show();
//					} else {
                    ConstCommon.intent = new Intent(HomeActivity.this, ClockActivity.class);
                    ConstCommon.intent.putExtra("ACTION", 1);
                    UtilsCommon.isClickForResult = true;
                    UtilsCommon.isCheckedForResult = false;
                    mCameraPreview.takePicture(HomeActivity.this);
//                    releaseCamera();
                    btnClockIn.setBackgroundResource(R.drawable.redbutton_unactiveted);
                    btnClockIn.setEnabled(false);
//					}
                } else {
                    ConstCommon.intent = new Intent(HomeActivity.this, ClockActivity.class);
                    ConstCommon.intent.putExtra("ACTION", 1);
                    UtilsCommon.isClickForResult = true;
                    UtilsCommon.isCheckedForResult = false;
                    mCameraPreview.takePicture(HomeActivity.this);
//                    releaseCamera();
                    ConstCommon.intent.putExtra("pathImage",
                            ConstCommon.pathPicture);
                    startActivity(ConstCommon.intent);
                }

            }
        });
        btnClockOut.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                daoEmpClock _daoEmpClock = new daoEmpClock();
                _daoEmpClock = employeeClockEntity.getStatusByID(ConstCommon.daoEmpWhenPinCode.getID());
                if (_daoEmpClock.getStatusClock() != null && _daoEmpClock.getStatusClock().length() > 0) {
                    if (_daoEmpClock.getStatusClock().toUpperCase().equals(ConstCommon.STR_CLOCK_OUT)) {
                        Toast.makeText(getBaseContext(), getResources().getString(R.string.you_must_to_clock_in_before_clock_out), Toast.LENGTH_LONG).show();
                    } else {
                        ConstCommon.intent = new Intent(HomeActivity.this, ClockActivity.class);
                        ConstCommon.intent.putExtra("ACTION", 4);
                        UtilsCommon.isClickForResult = true;
                        UtilsCommon.isCheckedForResult = false;
                        mCameraPreview.takePicture(HomeActivity.this);
                        btnClockOut.setBackgroundResource(R.drawable.greenbutton_unactiveted);
                        btnClockOut.setEnabled(false);
                    }
                } else {
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.you_must_to_clock_in_before_clock_out), Toast.LENGTH_LONG).show();
                }
            }
        });
        if (this.findFrontFacingCamera() != -1) {
            btnSwitchCamera.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    releaseCamera();
                    chooseCamera();
                }
            });
        } else {
            btnSwitchCamera.setEnabled(false);
        }
        imgMenu.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                ConstCommon.PROJECT_IS_SKIP = true;
                ConstCommon.PROJECT_SKIP = "TRUE";
                btnClockIn.setBackgroundResource(R.drawable.redbutton_activeted);
                btnClockOut.setBackgroundResource(R.drawable.greenbutton_activeted);
                btnClockIn.setEnabled(true);
                btnClockOut.setEnabled(true);
                showDialogToNotifySkip();
            }
        });

        ConstCommon.PROJECT_IS_SKIP = false;
        ConstCommon.PROJECT_SKIP = "FALSE";
        serviceIntent = new Intent(this, TimerService.class);
        startService(serviceIntent);
    }


    private Camera getCameraInstance() {
        Camera camera = null;
        try {
            camera = Camera.open(this.findBackFacingCamera());
        } catch (Exception e) {
        }
        return camera;
    }

    public void releaseCamera() {
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }

    public void chooseCamera() {
        if (isSelectCameraFront) {
            int cameraId = findBackFacingCamera();
            if (cameraId >= 0) {
                mCamera = Camera.open(cameraId);
                mCameraPreview.refreshCamera(mCamera);
            }
        } else {
            int cameraId = findFrontFacingCamera();
            if (cameraId >= 0) {
                mCamera = Camera.open(cameraId);
                mCameraPreview.refreshCamera(mCamera);
            }
        }
    }

    private int findFrontFacingCamera() {
        int cameraId = -1;
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                cameraId = i;
                isSelectCameraFront = true;
                break;
            }
        }
        return cameraId;
    }

    private int findBackFacingCamera() {
        int cameraId = -1;
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                cameraId = i;
                isSelectCameraFront = false;
                break;
            }
        }
        return cameraId;
    }

    @SuppressLint("InflateParams")
    private void showDialogToNotifySkip() {
//        File root = new File(mPath);
//        FilenameFilter pngFilter = new FilenameFilter() {
//            @SuppressLint("DefaultLocale")
//            public boolean accept(File dir, String name) {
//                return name.toLowerCase().endsWith(".jpg");
//            }
//
//            ;
//        };
//        File[] imageFiles = root.listFiles(pngFilter);
//        File imgFile = null;
//        if (imageFiles != null && imageFiles.length > 0) {
//            imgFile = new File(imageFiles[0].toString());
//        }

//        final Dialog dialogSkip = new Dialog(this);
//        dialogSkip.setContentView(R.layout.dialog_show_skip);
//        dialogSkip.setTitle(Html.fromHtml("<font size=\"6\" >Welcome</font>"));
//        TextView text = (TextView) dialogSkip.findViewById(R.id.tvName);
//        text.setText(ConstCommon.daoEmpWhenPinCode.getNameEmp());
////        //ImageView image = (ImageView) dialogSkip.findViewById(R.id.imgAvata);
////        if (imgFile != null && imgFile.exists()) {
////            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
////            ConstCommon.bitMapImageWhenSkip = myBitmap;
////            //image.setImageBitmap(myBitmap);
////        }
//
//        Button dialogButton = (Button) dialogSkip.findViewById(R.id.btnConfirm);
//        dialogButton.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialogSkip.dismiss();
//            }
//        });
//        dialogSkip.show();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder
                .setTitle("Welcome")
                .setMessage(ConstCommon.daoEmpWhenPinCode.getNameEmp())
                .setCancelable(false)
                .setNegativeButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub

                    }
                });

        AlertDialog alert = alertDialogBuilder.create();
        alert.show();

    }

    void initDatabase() {
        entity = new ProjectEntity(this);
        timesyncEntity = new TimeSyncEntity(this);
        projectEntity = new ProjectEntity(this);
        employeeEntity = new EmployeeEntity(this);
        employeeClockEntity = new EmployeeClockEntity(this);
    }


    @Override
    public void finish() {
        try {
            serviceIntent = new Intent(this, TimerService.class);
            stopService(serviceIntent);
        } catch (Exception e) {
            // TODO: handle exception
        }
        super.finish();
    }
}
