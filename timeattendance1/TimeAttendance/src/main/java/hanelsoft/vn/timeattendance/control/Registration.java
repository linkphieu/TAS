package hanelsoft.vn.timeattendance.control;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.a2000.tas.R;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.File;
import java.io.IOException;

import hanelsoft.vn.timeattendance.common.ConstCommon;
import hanelsoft.vn.timeattendance.common.UtilsCommon;
import hanelsoft.vn.timeattendance.model.DAO.daoEmp;
import hanelsoft.vn.timeattendance.model.entity.EmployeeEntity;
import hanelsoft.vn.timeattendance.model.helper.DatabaseHandler;
import hanelsoft.vn.timeattendance.model.helper.FileHelper;
import hanelsoft.vn.timeattendance.linkstech.helper.CameraPreview;

@SuppressWarnings("deprecation")
public class Registration extends Activity {
	private Camera mCamera;
	private CameraPreview mCameraPreview;
	static final long MAXIMG = 1;
	Handler mHandler;
	int countImages = 0;
	int flagUpload = 1;
	Bitmap mBitmap;
	boolean isUpload = false;
	EditText ID, Name, Phone, Address, Email;
	Gallery gallery;
	ImageButton imgRefresh, switchCamera;
	Button btnSubmit, btnCapture;
	DatabaseHandler dbHandler;
	ImageView Iv;
	private String getPath = null;
	private EmployeeEntity employeeEntity;
	String addEmpResponse;
	
	ImageButton btnSwitchCamera;
	Boolean isSelectCameraFront;
	public Registration() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		initDatabase();
		
		isSelectCameraFront = false;		
		btnSwitchCamera = (ImageButton) findViewById(R.id.btnSwitchCamera);
		
		mCamera = getCameraInstance();
	    mCameraPreview = new CameraPreview(this, mCamera);
	    FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
	    preview.addView(mCameraPreview);

		ID = (EditText) findViewById(R.id.ID);
		Name = (EditText) findViewById(R.id.NameEmp);
		Phone = (EditText) findViewById(R.id.Phone);
		Address = (EditText) findViewById(R.id.Address);
		Email = (EditText) findViewById(R.id.Email);

		btnSubmit = (Button) findViewById(R.id.btnSubmit);

		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.obj == "IMG") {
					Canvas canvas = new Canvas();
					canvas.setBitmap(mBitmap);
					Iv.setImageBitmap(mBitmap);
				}
			}
		};

		btnSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (ID.getText().toString().equals("")
						|| Name.getText().toString().equals("")
						|| Phone.getText().toString().equals("")
						|| Email.getText().toString().equals("")
						|| Address.getText().toString().equals("")) {

					Toast.makeText(Registration.this, "Please input all information!", Toast.LENGTH_SHORT).show();
				} else {
					
					mCameraPreview.takePicture();
					getPath=ConstCommon.pathPictureToUpload;
					isUpload = true;
					daoEmp mDaoEmp = new daoEmp();
					mDaoEmp.setID(ID.getText().toString());
					mDaoEmp.setNameEmp(Name.getText().toString());
					employeeEntity.add(mDaoEmp);
					uploadImage();
				}

			}
		});
		
		btnSwitchCamera.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				releaseCamera();
				chooseCamera();
			}
		});
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
	public void uploadImage() {
		String strBase64 = "";
		try {
			strBase64 = UtilsCommon.encodeImage(ConstCommon.pathPictureToUpload);
		} catch (Exception e) {
			// TODO: handle exception
		}
		new AsynSubmit().execute(strBase64, ID.getText().toString(), Name
				.getText().toString(), Phone.getText().toString(), Email
				.getText().toString(), Address.getText().toString());
	}

	void initDatabase() {
		try {
			employeeEntity = new EmployeeEntity(this);
			dbHandler = new DatabaseHandler(this);
			dbHandler.createDataBase();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Camera getCameraInstance() {
        Camera camera = null;
        try {
            camera = Camera.open(this.findBackFacingCamera());
        } catch (Exception e) {
            // cannot get camera or does not exist
        }
        return camera;
	}
	
	private void SetInputEmpty()
	{
		ID.setText("");
		Name.setText("");
		Phone.setText("");
		Email.setText("");
		Address.setText("");
	}

	private class AsynSubmit extends AsyncTask<String, Void, Void> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(String... params) {
			Submit(params[0], params[1], params[2], params[3], params[4],
					params[5]);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			try {
				JSONArray jar = new JSONArray(addEmpResponse);
				JSONObject obj = jar.getJSONObject(0);
				String success = obj.getString("Success");
				if (success.equals("1")) {
					flagUpload = 1;
						isUpload = false;
						Toast.makeText(Registration.this, "New employee is registered successfully", Toast.LENGTH_SHORT).show();
						SetInputEmpty();
				} else if (success.equals("2")) {
					Toast.makeText(
							Registration.this,
							"Failed to register new employee. Please try again!",
							Toast.LENGTH_SHORT).show();
					SetInputEmpty();
				} else if (success.equals("0")) {
					flagUpload = 2;
					File file = new File(getPath);
					if (file.exists()) {
						file.delete();
					}
					Toast.makeText(Registration.this, "The entered Pin Code is exists. Please try another one!", Toast.LENGTH_SHORT).show();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void Submit(String encodedImage, String ID, String Name,
			String Phone, String Email, String Address) {
		try {
			SoapObject so = new SoapObject(ConstCommon.NAMESPACE,
					ConstCommon.METHOD_NAME_ADD_EMPLOYEE);
			so.addProperty("empID", ID);
			so.addProperty("empName", Name);
			so.addProperty("empPhone", Phone);
			so.addProperty("empEmail", Email);
			so.addProperty("empAddress", Address);
			so.addProperty("empImageBase64String", encodedImage);
			SoapSerializationEnvelope sse = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			new MarshalBase64().register(sse);
			sse.dotNet = true;
			sse.setOutputSoapObject(so);
			HttpTransportSE htse = new HttpTransportSE(FileHelper.readFromFile(this));
			htse.call(ConstCommon.SOAP_ACTION_ADD_EMPLOYEE, sse);
			SoapPrimitive response = (SoapPrimitive) sse.getResponse();
			addEmpResponse = response.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
