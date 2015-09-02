package hanelsoft.vn.timeattendance.view;

import android.R;
import android.R.drawable;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import hanelsoft.vn.timeattendance.common.ConstCommon;
import hanelsoft.vn.timeattendance.control.HomeActivity;
import hanelsoft.vn.timeattendance.model.DAO.daoEmp;
import hanelsoft.vn.timeattendance.model.entity.EmployeeEntity;
import hanelsoft.vn.timeattendance.model.helper.DatabaseHandler;
import hanelsoft.vn.timeattendance.model.helper.FileHelper;

public class CustomKeyboard {
	public final static int CodEnter = 55000;
	public final static int CodeClear = 55006;
	private KeyboardView mKeyboardView;
	private Activity mHostActivity;
	Context context;
	byte[] array = null;
	String encodeImage;
	EmployeeEntity empEntity;
	ArrayList<daoEmp> arrayEmp;
	String str;
	ProgressDialog dialog = null;
	DatabaseHandler dbHandler;
	private OnKeyboardActionListener mOnKeyboardActionListener = new OnKeyboardActionListener() {

		@Override
		public void onKey(int primaryCode, int[] keyCodes) {
			View focusCurrent = mHostActivity.getWindow().getCurrentFocus();
			if (focusCurrent == null
					|| focusCurrent.getClass() != EditText.class)
				return;
			EditText edittext = (EditText) focusCurrent;
			Editable editable = edittext.getText();
			int start = edittext.getSelectionStart();
			if (primaryCode == CodEnter) {
				// ProjectEntity proj = new ProjectEntity(mHostActivity);
				// ArrayList<daoProject> dao = new ArrayList<daoProject>();
				// dao = proj.getAllProject();
				Log.d("Emid", ConstCommon.EmID);
				if ((ConstCommon.pin1 != "") && (ConstCommon.pin2 != "")
						&& (ConstCommon.pin3 != "") && (ConstCommon.pin4 != "")
						&& (ConstCommon.EmID != "")) {
					empEntity = new EmployeeEntity(mHostActivity);
					arrayEmp = new ArrayList<daoEmp>();
					arrayEmp = empEntity.getByID(ConstCommon.EmID);
					if (arrayEmp.size() == 0) {
						Toast.makeText(mHostActivity,
								"Your code is not correct", Toast.LENGTH_LONG)
								.show();
					} else {
						ConstCommon.daoEmpWhenPinCode = arrayEmp.get(0);
						Intent intent = new Intent(mHostActivity,
								HomeActivity.class);
						mHostActivity.startActivity(intent);
					}

				} else {
					Toast.makeText(mHostActivity, "Please enter your pin code",
							Toast.LENGTH_SHORT).show();
				}

			} else if (primaryCode == CodeClear) {

				ViewGroup group = (ViewGroup) mHostActivity
						.findViewById(com.a2000.tas.R.id.rl1);
				for (int i = 0, count = group.getChildCount(); i < count; ++i) {
					View view = group.getChildAt(i);
					if (view instanceof EditText) {
						((EditText) view).setText("");
						ConstCommon.resetValue();
					}
				}

			} else {
				editable.insert(start, Character.toString((char) primaryCode));
			}
		}

		@Override
		public void onPress(int arg0) {
		}

		@Override
		public void onRelease(int primaryCode) {
		}

		@Override
		public void onText(CharSequence text) {
		}

		@Override
		public void swipeDown() {
		}

		@Override
		public void swipeLeft() {
		}

		@Override
		public void swipeRight() {
		}

		@Override
		public void swipeUp() {
		}
	};

	/**
	 * @param host
	 *            The hosting activity.
	 * @param viewid
	 *            The id of the KeyboardView.
	 * @param layoutid
	 *            The id of the xml file containing the keyboard layout.
	 */
	public CustomKeyboard(Activity host, int viewid, int layoutid) {
		mHostActivity = host;
		mKeyboardView = (KeyboardView) mHostActivity.findViewById(viewid);
		mKeyboardView.setKeyboard(new Keyboard(mHostActivity, layoutid));
		mKeyboardView.setPreviewEnabled(false); // NOTE Do not show the preview
												// balloons
		mKeyboardView.setOnKeyboardActionListener(mOnKeyboardActionListener);
		// Hide the standard keyboard initially
		mHostActivity.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		try {
			Log.d("TAG", "Create Database");
			dbHandler = new DatabaseHandler(mHostActivity);
			dbHandler.createDataBase();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/** Returns whether the CustomKeyboard is visible. */
	public boolean isCustomKeyboardVisible() {
		return mKeyboardView.getVisibility() == View.VISIBLE;
	}

	/**
	 * Make the CustomKeyboard visible, and hide the system keyboard for view v.
	 */
	public void showCustomKeyboard(View v) {
		mKeyboardView.setVisibility(View.VISIBLE);
		mKeyboardView.setEnabled(true);
		if (v != null)
			((InputMethodManager) mHostActivity
					.getSystemService(Activity.INPUT_METHOD_SERVICE))
					.hideSoftInputFromWindow(v.getWindowToken(), 0);
	}

	/** Make the CustomKeyboard invisible. */
	public void hideCustomKeyboard() {
		mKeyboardView.setVisibility(View.GONE);
		mKeyboardView.setEnabled(false);
	}

	public void registerEditText(int resid) {
		EditText edittext = (EditText) mHostActivity.findViewById(resid);
		edittext.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus)
					showCustomKeyboard(v);
				else
					hideCustomKeyboard();
			}
		});
		edittext.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showCustomKeyboard(v);
			}
		});

		edittext.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				EditText edittext = (EditText) v;
				int inType = edittext.getInputType(); // Backup the input type
				edittext.setInputType(InputType.TYPE_NULL); // Disable standard
															// keyboard
				edittext.onTouchEvent(event); // Call native handler
				edittext.setInputType(inType); // Restore input type
				return true; // Consume touch event
			}
		});
		edittext.setInputType(edittext.getInputType()
				| InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
	}

	public static byte[] streamToBytes(InputStream is) {
		ByteArrayOutputStream os = new ByteArrayOutputStream(1024);
		byte[] buffer = new byte[1024];
		int len;
		try {
			while ((len = is.read(buffer)) >= 0) {
				os.write(buffer, 0, len);
			}
		} catch (java.io.IOException e) {
		}
		return os.toByteArray();
	}

	private void getProject(String encodedImage) {
		try {
			SoapObject so = new SoapObject(ConstCommon.NAMESPACE,
					ConstCommon.METHOD_NAME_UPLOAD);
			so.addProperty("myBase64String", encodedImage);
			so.addProperty("LocationX", String.valueOf(ConstCommon.latitude));
			so.addProperty("LocationY", String.valueOf(ConstCommon.longitude));
			so.addProperty("action", ConstCommon.action);
			so.addProperty("EmpID", ConstCommon.EmID);
			so.addProperty("ProjectID", ConstCommon.ProjectID);
			SoapSerializationEnvelope sse = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			new MarshalBase64().register(sse);
			sse.dotNet = true;
			sse.setOutputSoapObject(so);
			HttpTransportSE htse = new HttpTransportSE(FileHelper.readFromFile(context));
			htse.call(ConstCommon.SOAP_ACTION_UPLOAD, sse);
			SoapPrimitive response = (SoapPrimitive) sse.getResponse();
			str = response.toString();
			System.out.println("Server Response" + str);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private class AsyncCallWS extends AsyncTask<String, Void, Void> {
		@Override
		protected Void doInBackground(String... params) {
			try {
				getProject(encodeImage);
			} catch (Exception e) {
				dialog.dismiss();
				AlertDialog.Builder alertDlg = new AlertDialog.Builder(
						mHostActivity);
				alertDlg.setMessage("Can't connect to server")
						.setIcon(R.drawable.ic_dialog_alert)
						.setPositiveButton("OK",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										mHostActivity.finish();
									}
								});

				alertDlg.show();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			try {
				JSONArray job = new JSONArray(str);
				JSONObject obj = job.getJSONObject(0);
				String success = obj.getString("Success");
				if (success.equals("0")) {
					dialog.dismiss();
					AlertDialog.Builder alertDlg = new AlertDialog.Builder(
							mHostActivity);
					alertDlg.setMessage(
							"Project is not located at this location")
							.setIcon(drawable.ic_dialog_alert)
							.setPositiveButton("OK",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											ViewGroup group = (ViewGroup) mHostActivity
													.findViewById(com.a2000.tas.R.id.relativeLayout1);
											for (int i = 0, count = group
													.getChildCount(); i < count; ++i) {
												View view = group.getChildAt(i);
												if (view instanceof EditText) {
													((EditText) view)
															.setText("");
													ConstCommon.resetValue();
												}
											}
										}
									});
					alertDlg.show();
				} else if (success.equals("1")) {
					dialog.dismiss();
					AlertDialog.Builder alertDlg = new AlertDialog.Builder(
							mHostActivity);
					TextView title = new TextView(mHostActivity);
					String nameAction = "";
					if (ConstCommon.action == 1) {
						nameAction = "Clock in is done! Thank you! Have a nice day!";
					} else if (ConstCommon.action == 2) {
						nameAction = "Out for lunch is done! Thank you! Have a nice day!";
					} else if (ConstCommon.action == 3) {
						nameAction = "Back from lunch is done! Thank you! Have a nice day!";
					} else if (ConstCommon.action == 4) {
						nameAction = "Clock out is done! Thank you! Have a nice day!";
					}
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					String currentDateandTime = sdf.format(new Date());
					final Dialog dialog = new Dialog(mHostActivity);
					TextView txtNameEmp, txtAction, txtTime;
					ImageView image;
					Button btnOK;
					dialog.setContentView(com.a2000.tas.R.layout.message_clock_success);
					dialog.setTitle("Information!");
					dialog.setCancelable(false);
					txtNameEmp = (TextView) dialog
							.findViewById(com.a2000.tas.R.id.txtNameEmp);
					txtTime = (TextView) dialog
							.findViewById(com.a2000.tas.R.id.txtTime);
					txtAction = (TextView) dialog
							.findViewById(com.a2000.tas.R.id.txtAction);
					btnOK = (Button) dialog
							.findViewById(com.a2000.tas.R.id.btnOK);
					image = (ImageView) dialog
							.findViewById(com.a2000.tas.R.id.image);
					BitmapFactory.Options options = new BitmapFactory.Options();
					options.inSampleSize = 1;
					final Bitmap bitmap = BitmapFactory.decodeFile(
							ConstCommon.pathImage, options);
					image.setImageBitmap(bitmap);
					txtNameEmp.setText(ConstCommon.nameEmp);
					txtAction.setText(nameAction);
					txtTime.setText(currentDateandTime);
					dialog.show();
					btnOK.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							mHostActivity.finish();
						}
					});

				} else {
					dialog.dismiss();
					AlertDialog.Builder alertDlg = new AlertDialog.Builder(
							mHostActivity);
					alertDlg.setMessage("Clock fail")
							.setIcon(R.drawable.ic_dialog_alert)
							.setPositiveButton("OK",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											mHostActivity.finish();
										}
									});

					alertDlg.show();
				}
			} catch (Exception e) {
			}

		}
	}

}
