package hanelsoft.vn.timeattendance.control;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.a2000.tas.R;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import hanelsoft.vn.timeattendance.common.ConstCommon;
import hanelsoft.vn.timeattendance.common.RequestHelper;
import hanelsoft.vn.timeattendance.common.UtilsCommon;
import hanelsoft.vn.timeattendance.model.DAO.DaoProjectLocation;
import hanelsoft.vn.timeattendance.model.DAO.daoClock;
import hanelsoft.vn.timeattendance.model.DAO.daoEmp;
import hanelsoft.vn.timeattendance.model.DAO.daoProject;
import hanelsoft.vn.timeattendance.model.DAO.daoTimeSync;
import hanelsoft.vn.timeattendance.model.entity.EmployeeEntity;
import hanelsoft.vn.timeattendance.model.entity.ProjectEntity;
import hanelsoft.vn.timeattendance.model.entity.ProjectLocationEntity;
import hanelsoft.vn.timeattendance.model.entity.TimeSyncEntity;
import hanelsoft.vn.timeattendance.model.entity.TimesheetEntity;
import hanelsoft.vn.timeattendance.model.helper.FileHelper;
import hanelsoft.vn.timeattendance.view.CustomKeyboard;

public class PincodeActivity extends Activity {
    CustomKeyboard mCustomKeyboard;
    EditText pin1, pin2, pin3, pin4;
    TextView txtCurrentDate, textView1;
    protected PopupMenu popup;
    ImageButton imgMenu;
    int rowID;
    private daoProject mDaoProject;
    TimeSyncEntity timesyncEntity;
    ProjectEntity projectEntity;
    EmployeeEntity employeeEntity;
    ProjectLocationEntity projectLocationEntity;
    private static String TimeSync, responseJson;
    ProgressDialog dialogGetData = null, dlgLogin = null;
    ProgressDialog dialog = null;
    Dialog dialogLogin;
    ArrayList<daoProject> arrayList = new ArrayList<daoProject>();
    ArrayList<daoClock> arrDao = new ArrayList<daoClock>();
    String mPath;
    String str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pincode);
        mCustomKeyboard = new CustomKeyboard(this, R.id.keyboardview,
                R.drawable.keyboard);
        initEntity();
        pin1 = (EditText) findViewById(R.id.pin1);
        pin2 = (EditText) findViewById(R.id.pin2);
        pin3 = (EditText) findViewById(R.id.pin3);
        pin4 = (EditText) findViewById(R.id.pin4);

        // Set current Date
        txtCurrentDate = (TextView) findViewById(R.id.currentDate);
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMMM dd");
        String currentDate = sdf.format(new Date());
        txtCurrentDate.setText(currentDate);

        // Set text view
        textView1 = (TextView) findViewById(R.id.textView1);
        String text = "Please enter your <b>'PIN CODE'</b> to proceed";
        textView1.setText(Html.fromHtml(text));

        imgMenu = (ImageButton) findViewById(R.id.menu);
        mCustomKeyboard.registerEditText(R.id.pin1);
        mCustomKeyboard.registerEditText(R.id.pin2);
        mCustomKeyboard.registerEditText(R.id.pin3);
        mCustomKeyboard.registerEditText(R.id.pin4);

        pin1.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (pin1.length() >= 1)
                    pin2.requestFocus();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                ConstCommon.pin1 = pin1.getText().toString();

            }
        });
        pin2.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (pin2.length() >= 1)
                    pin3.requestFocus();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                ConstCommon.pin2 = pin2.getText().toString();
            }
        });
        pin3.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (pin3.length() >= 1)
                    pin4.requestFocus();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                ConstCommon.pin3 = pin3.getText().toString();
            }
        });
        pin4.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                ConstCommon.pin4 = pin4.getText().toString();
                if ((pin4.length() > 0) && (pin1.length() > 0)
                        && (pin2.length() > 0) && (pin3.length() > 0)) {
                    ConstCommon.EmID = ConstCommon.pin1 + ConstCommon.pin2
                            + ConstCommon.pin3 + ConstCommon.pin4;
                } else {
                    pin1.requestFocus();
                }
            }
        });
        pin1.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                pin1.requestFocus();
                return false;
            }
        });
        pin2.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                pin2.requestFocus();
                return false;
            }
        });
        pin3.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                pin3.requestFocus();
                return false;
            }
        });
        pin4.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                pin4.requestFocus();
                return false;
            }
        });
        imgMenu.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (popup == null) {
                    popup = new PopupMenu(PincodeActivity.this, imgMenu);
                    popup.getMenuInflater().inflate(R.menu.main,
                            popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            return onOptionsItemSelected(item);
                        }
                    });
                }
                popup.show();
            }
        });
        FileHelper.writeToFile(this, "http://118.189.60.23:84/TAS/Service1.asmx?WSDL");
    }

    private Boolean exit = false;

    @SuppressLint("NewApi")
    @Override
    protected void onResume() {
        ConstCommon.EmID = "";
        ViewGroup group = (ViewGroup) findViewById(com.a2000.tas.R.id.rl1);
        for (int i = 0, count = group.getChildCount(); i < count; ++i) {
            View view = group.getChildAt(i);
            if (view instanceof EditText) {
                ((EditText) view).setText("");
                ConstCommon.resetValue();
            }
        }
        if (ConstCommon.flag) {
            checkInternet();
        }

        Settings.System.putInt(getContentResolver(), Settings.System.AUTO_TIME,
                1);
        Settings.System.putInt(getContentResolver(),
                Settings.System.AUTO_TIME_ZONE, 1);

        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if (exit) {
//			PincodeActivity.this.finish();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Press Back again to Exit.", Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }

    }

    private void initEntity() {
        timesyncEntity = new TimeSyncEntity(PincodeActivity.this);
        projectEntity = new ProjectEntity(PincodeActivity.this);
        employeeEntity = new EmployeeEntity(PincodeActivity.this);
        projectLocationEntity = new ProjectLocationEntity(PincodeActivity.this);

    }

    @Override
    public boolean onKeyUp(int keycode, KeyEvent e) {
        switch (keycode) {
            case KeyEvent.KEYCODE_MENU:
                if (popup == null) {
                    popup = new PopupMenu(PincodeActivity.this, imgMenu);
                    popup.getMenuInflater().inflate(R.menu.main, popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            return onOptionsItemSelected(item);
                        }
                    });
                }
                popup.show();
                return true;
        }
        return super.onKeyUp(keycode, e);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.pincode, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.Sync:
                if (UtilsCommon.haveNetworkConnection(PincodeActivity.this)) {
                    rowID = 1;
                    ArrayList<daoTimeSync>
                            arrTimeSync = timesyncEntity.getTime();
                    if (arrTimeSync.size() == 0) {
                        TimeSync = "1990/1/1 00:00:00";
                        dialogGetData = ProgressDialog.show(PincodeActivity.this,
                                "", "Please wait");
                        AsyncGetData task = new AsyncGetData();
                        task.execute();
                    } else {

                        TimeSync = arrTimeSync.get(0).getTimeSync();
                        dialogGetData = ProgressDialog.show(PincodeActivity.this,
                                "", "Please wait");
                        System.out.println("Time Sync: " + TimeSync);
                        AsyncGetData task = new AsyncGetData();
                        task.execute();
                    }

                } else {
                    Toast.makeText(PincodeActivity.this,
                            "Error to connect network!", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.Push:
                if (UtilsCommon.haveNetworkConnection(PincodeActivity.this)) {
                    TimesheetEntity timeEnt = new TimesheetEntity(this);
                    arrDao = timeEnt.getByStatus(0);
                    if (arrDao.size() > 0) {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PincodeActivity.this);
                        alertDialogBuilder
                                .setMessage("Are you sure want to sync the data?")
                                .setCancelable(true)
                                .setPositiveButton("Yes",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,
                                                                int id) {
                                                PincodeActivity.this.dialog = ProgressDialog.show(PincodeActivity.this, "", "Synchronizing...");
                                                AsynSyncData sync = new AsynSyncData();
                                                sync.execute();
                                            }
                                        })
                                .setNegativeButton("No",
                                        new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialog,
                                                                int which) {

                                            }
                                        });
                        AlertDialog alert = alertDialogBuilder.create();
                        alert.show();
                    }
                }
                break;
            case R.id.Registration:
                this.loginAction("registration");
                break;
            case R.id.action_settings:
                //this.loginAction("setting");
                this.showSetting();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loginAction(final String action) {
        if (UtilsCommon.haveNetworkConnection(PincodeActivity.this)) {
            dialogLogin = new Dialog(PincodeActivity.this);
            final EditText UserName, Password;

            Button btnLogin;
            dialogLogin
                    .setContentView(com.a2000.tas.R.layout.dialog_login_admin);
            dialogLogin.setTitle("Registration/Logon");
            dialogLogin.setCancelable(true);
            UserName = (EditText) dialogLogin
                    .findViewById(com.a2000.tas.R.id.UserName);
            Password = (EditText) dialogLogin
                    .findViewById(com.a2000.tas.R.id.Password);
            btnLogin = (Button) dialogLogin
                    .findViewById(com.a2000.tas.R.id.Login);
            dialogLogin.show();
            btnLogin.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    dlgLogin = new ProgressDialog(PincodeActivity.this);
                    dlgLogin.setMessage("Processing...");
                    dlgLogin.setCancelable(false);
                    dlgLogin.show();
                    if (action == "registration") {
                        AsynCheckLogin task = new AsynCheckLogin();
                        task.execute(UserName.getText().toString(), Password
                                .getText().toString(), action);
                    } else if (action == "setting") {
                        if (RequestHelper.checkLogin(PincodeActivity.this, UserName.getText().toString(), Password.getText().toString())) {
                            showSetting();
                        }
                    }
                }
            });
        } else {
            Toast.makeText(PincodeActivity.this, "No internet connection",
                    Toast.LENGTH_SHORT);
        }
    }

    private class AsyncGetData extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // isLoadComplete = false;
        }

        @Override
        protected Void doInBackground(String... params) {
            getData(TimeSync);
            dialogGetData.dismiss();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            //new AsynGetDataImage().execute();
        }
    }

    private class AsynGetDataImage extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            // TODO Auto-generated method stub
            getDataEmpImage(TimeSync, rowID);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (rowID <= ConstCommon.CountTotalImage) {
                new AsynGetDataImage().execute();
            } else {
                if (dialogGetData != null)
                    try {
                        dialogGetData.dismiss();
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
            }
            super.onPostExecute(result);
        }

    }

    private class AsynSyncData extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            for (int i = 0; i < arrDao.size(); i++) {
                String image = UtilsCommon.encodeImage(arrDao.get(i).getImage());
                getProject(String.valueOf(arrDao.get(i).getEmployeeID()),
                        String.valueOf(arrDao.get(i).getLat()),
                        String.valueOf(arrDao.get(i).getLng()), image,
                        arrDao.get(i).getDatetime(), arrDao.get(i).getAction(), arrDao.get(i).getProjectID(), arrDao.get(i).getLocationID());

                if (i == arrDao.size() - 1) {
                    dialog.dismiss();
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(PincodeActivity.this, "Sync data successfully", Toast.LENGTH_SHORT).show();
                        }
                    });

                    TimesheetEntity time = new TimesheetEntity(PincodeActivity.this);
                    time.updateAttachPath("1");

                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

        }
    }

    private class AsynCheckLogin extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            checkLogin(params[0], params[1], params[2]);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // dialogLogin.dismiss();
        }
    }

    public void getData(String Time) {
        SoapObject request = new SoapObject(ConstCommon.NAMESPACE,
                ConstCommon.METHOD_NAME_GET_DATA);
        PropertyInfo empAPI = new PropertyInfo();
        empAPI.setName("date");
        empAPI.setValue(Time);
        empAPI.setType(double.class);
        request.addProperty(empAPI);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(
                FileHelper.readFromFile(this));
        try {
            androidHttpTransport.call(ConstCommon.SOAP_ACTION_GET_DATA,
                    envelope);
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            responseJson = response.toString();
            JSONObject job = new JSONObject(responseJson);
            System.out
                    .println("--------------------------------Luong VD ----------------------------");
            System.out.println(job);
            System.out
                    .println("--------------------------------Luong VD ----------------------------");
            int success = job.getInt("Success");
            ConstCommon.CountTotalImage = job.getInt("CountTotalEmpImage");
            mDaoProject = new daoProject();
            mDaoProject.setNameProject("Select");
            arrayList.add(mDaoProject);
            if (success == 1) {
                // Data Project
                JSONArray jArray = job.getJSONArray("DataProject");
                for (int i = 0; i < jArray.length(); i++) {

                    mDaoProject = new daoProject();
                    mDaoProject.setID(jArray.getJSONObject(i).getString("ID"));
                    mDaoProject.setNameProject(jArray.getJSONObject(i)
                            .getString("NameProject"));
                    mDaoProject.setAllowSkip(jArray.getJSONObject(i).getString(
                            "AllowSkip"));

                    projectEntity.add(mDaoProject);
                }
                // Data Project Location
                JSONArray jsonProjectLocation = job
                        .getJSONArray("DataProjectLocation");
                for (int i = 0; i < jsonProjectLocation.length(); i++) {
                    DaoProjectLocation daoProjectLocation = new DaoProjectLocation();
                    daoProjectLocation.setProjectID(jsonProjectLocation
                            .getJSONObject(i).getString("projectID"));
                    daoProjectLocation.setLocationX(jsonProjectLocation
                            .getJSONObject(i).getString("LocationX"));
                    daoProjectLocation.setLocationY(jsonProjectLocation
                            .getJSONObject(i).getString("LocationY"));
                    daoProjectLocation.setLocationID(jsonProjectLocation
                            .getJSONObject(i).getString("LocationID"));
                    daoProjectLocation.setAddress(jsonProjectLocation
                            .getJSONObject(i).getString("Address"));
                    ProjectLocationEntity projectLocationEntity = new ProjectLocationEntity(
                            PincodeActivity.this);
                    projectLocationEntity.add(daoProjectLocation);
                }
                // Delete Employee
                JSONArray jArrayDelEmp = job.getJSONArray("DataEmployeeDelete");
                for (int i = 0; i < jArrayDelEmp.length(); i++) {
                    EmployeeEntity empEn = new EmployeeEntity(
                            PincodeActivity.this);
                    empEn.deleteByID(jArrayDelEmp.getJSONObject(i).getString(
                            "RowID"));
                }
                // Data Project Delete
                JSONArray jArrayDelProj = job.getJSONArray("DataProjectDelete");
                for (int i = 0; i < jArrayDelProj.length(); i++) {
                    ProjectEntity projectEnt = new ProjectEntity(this);
                    projectEnt.deleteByID(jArrayDelProj.getJSONObject(i)
                            .getString("RowID"));
                }
                ArrayList<daoTimeSync> arrTimeSync = new ArrayList<daoTimeSync>();
                arrTimeSync = timesyncEntity.getTime();
                String timeSync = job.getString("TimeSync");
                if (arrTimeSync.size() == 0) {
                    daoTimeSync time = new daoTimeSync();
                    time.setTimeSync(timeSync);
                    timesyncEntity.add(time);
                } else {
                    daoTimeSync time = new daoTimeSync();
                    time.setTimeSync(timeSync);
                    timesyncEntity.update(time);
                }

                // DataEmployee
                JSONArray jArrayEmp = job.getJSONArray("DataEmployee");
                for (int i = 0; i < jArrayEmp.length(); i++) {
                    String IDcheck = jArrayEmp.getJSONObject(i).getString("ID");
                    ArrayList<daoEmp> listEmp = new ArrayList<daoEmp>();
                    listEmp = employeeEntity.getByID(IDcheck);
                    if (listEmp.size() > 0) {
                        employeeEntity.deleteByID(IDcheck);
                    }
                    daoEmp mDaoEmp = new daoEmp();
                    mDaoEmp.setID(jArrayEmp.getJSONObject(i).getString("ID"));
                    mDaoEmp.setNameEmp(jArrayEmp.getJSONObject(i).getString(
                            "Name"));
                    employeeEntity.add(mDaoEmp);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void checkLogin(String userName, String password, String action) {
        try {
            SoapObject so = new SoapObject(ConstCommon.NAMESPACE,
                    ConstCommon.METHOD_NAME_CHECK_LOGIN);
            so.addProperty("Username", userName);
            so.addProperty("Password", password);
            SoapSerializationEnvelope sse = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            new MarshalBase64().register(sse);
            sse.dotNet = true;
            sse.setOutputSoapObject(so);
            HttpTransportSE htse = new HttpTransportSE(FileHelper.readFromFile(this));
            htse.call(ConstCommon.SOAP_ACTION_CHECK_LOGIN, sse);
            SoapPrimitive response = (SoapPrimitive) sse.getResponse();
            String strResponse = response.toString();
            JSONArray jar = new JSONArray(strResponse);
            JSONObject obj = jar.getJSONObject(0);
            String success = obj.getString("Success");
            if (success.equals("1")) {
                dlgLogin.dismiss();
                dialogLogin.dismiss();
                if (action == "registration") {
                    Intent intent = new Intent(PincodeActivity.this,
                            Registration.class);
                    startActivity(intent);
                } else if (action == "setting") {
                }
            } else if (success.equals("2")) {
                dlgLogin.dismiss();
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(
                                PincodeActivity.this,
                                "UserName or Password is not correct. Please try again!",
                                Toast.LENGTH_SHORT).show();
                    }
                });

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showSetting() {
        final Dialog dialogInformation = new Dialog(PincodeActivity.this);
        final EditText urlText;
        Button btnOK, btnCancel;
        dialogInformation.setContentView(R.layout.setting_url);
        btnOK = (Button) dialogInformation.findViewById(R.id.btnOK);
        btnCancel = (Button) dialogInformation.findViewById(R.id.btnCancel);
        urlText = (EditText) dialogInformation.findViewById(R.id.url_text);
        urlText.setText(FileHelper.readFromFile(PincodeActivity.this));
        dialogInformation.setTitle("Web service URL:");
        btnOK.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String url = urlText.getText().toString();
                FileHelper.writeToFile(PincodeActivity.this, url);
                dialogInformation.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialogInformation.dismiss();
            }
        });
        try {
            dialogInformation.show();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public void getDataEmpImage(String Time, int rowid) {

        SoapObject so = new SoapObject(ConstCommon.NAMESPACE,
                ConstCommon.METHOD_NAME_GETDATA_EMPIMAGE);
        so.addProperty("date", Time);
        so.addProperty("rowid", rowid);
        SoapSerializationEnvelope sse = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        new MarshalBase64().register(sse);
        sse.dotNet = true;
        sse.setOutputSoapObject(so);
        try {
            HttpTransportSE htse = new HttpTransportSE(FileHelper.readFromFile(this));
            htse.call(ConstCommon.SOAP_ACTION_GETDATA_EMPIMAGE, sse);
            SoapPrimitive response = (SoapPrimitive) sse.getResponse();
            String strResponse = response.toString();
            System.out.println(strResponse);
            JSONObject job = new JSONObject(strResponse);
//			int success = job.getInt("Success");
//			if (success == 1) {
//				JSONArray jArray = job.getJSONArray("DataEmpImageReturn");
//
//				for (int i = 0; i < jArray.length(); i++) {
////					UtilsCommon.DecodeImage(PincodeActivity.this, jArray
////							.getJSONObject(i).getString("image"), jArray
////							.getJSONObject(i).getString("empID"), mPath, i);
//					rowID++;
//					showPercentDialog(rowID, ConstCommon.CountTotalImage);
//					System.out.println("Length:" + jArray.length());
//				}
//			}
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void getProject(String empID, String Lat, String Lng,
                            String encodedImage, String Time, String Action, String projectID,
                            String LocationID) {
        try {
            SoapObject so = new SoapObject(ConstCommon.NAMESPACE,
                    ConstCommon.METHOD_NAME_SYNCDATA);
            so.addProperty("myBase64String", encodedImage);
            so.addProperty("LocationX", Lat);
            so.addProperty("LocationY", Lng);
            so.addProperty("action", Action);
            so.addProperty("EmpID", empID);
            so.addProperty("Time", Time);
            so.addProperty("ProjectID", projectID);
            so.addProperty("LocationID", LocationID);
            SoapSerializationEnvelope sse = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            new MarshalBase64().register(sse);
            sse.dotNet = true;
            sse.setOutputSoapObject(so);
            HttpTransportSE htse = new HttpTransportSE(FileHelper.readFromFile(this));
            htse.call(ConstCommon.SOAP_ACTION_SYNCDATA, sse);
            SoapPrimitive response = (SoapPrimitive) sse.getResponse();
            str = response.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showPercentDialog(final int i, final int size) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    float percent = (float) (i) / (size) * 100;
                    dialogGetData.setMessage(String.format("%.2f%%", percent));
                } catch (Exception e) {
                    // TODO: handle exception
                }

            }
        });
    }

    public void checkInternet() {

        if (!UtilsCommon.haveNetworkConnection(PincodeActivity.this)) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    PincodeActivity.this);
            TextView title = new TextView(this);
            title.setGravity(Gravity.CENTER);
            title.setText("Mobile network is disabled at the moment. Do you want to enable now?");
            title.setTextSize(20);
            alertDialogBuilder
                    .setCustomTitle(title)
                    .setCancelable(false)
                    .setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    dialog.dismiss();
                                    AlertDialog.Builder alertDlg = new AlertDialog.Builder(
                                            PincodeActivity.this);
                                    alertDlg.setMessage(
                                            "Select connection type you want to use!")
                                            .setCancelable(true)
                                            .setNegativeButton(
                                                    "WIFI",
                                                    new DialogInterface.OnClickListener() {

                                                        @Override
                                                        public void onClick(
                                                                DialogInterface dialog,
                                                                int which) {
                                                            PincodeActivity.this
                                                                    .startActivity(new Intent(
                                                                            Settings.ACTION_WIFI_SETTINGS));
                                                        }
                                                    })
                                            .setPositiveButton(
                                                    "MOBILE DATA",
                                                    new DialogInterface.OnClickListener() {

                                                        @Override
                                                        public void onClick(
                                                                DialogInterface dialog,
                                                                int which) {
                                                            dialog.dismiss();
                                                            PincodeActivity.this
                                                                    .startActivity(new Intent(
                                                                            Settings.ACTION_DATA_ROAMING_SETTINGS));
                                                        }
                                                    });
                                    AlertDialog alert = alertDlg.create();
                                    alert.show();
                                }
                            })
                    .setNegativeButton("No",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.dismiss();
                                    ArrayList<daoProject> array = new ArrayList<daoProject>();
                                    array = projectEntity.getAllProject();
                                    if (array.size() == 0) {
                                        AlertDialog.Builder alr = new AlertDialog.Builder(
                                                PincodeActivity.this);
                                        alr.setMessage(
                                                "You must connect to the Internet for the first time synchronization!")
                                                .setCancelable(false)
                                                .setNegativeButton(
                                                        "OK",
                                                        new DialogInterface.OnClickListener() {

                                                            @Override
                                                            public void onClick(
                                                                    DialogInterface dialog,
                                                                    int which) {
                                                                AlertDialog.Builder alertDlg = new AlertDialog.Builder(
                                                                        PincodeActivity.this);
                                                                alertDlg.setMessage(
                                                                        "Select connection type you want to use!")
                                                                        .setCancelable(
                                                                                false)
                                                                        .setNegativeButton(
                                                                                "WIFI",
                                                                                new DialogInterface.OnClickListener() {

                                                                                    @Override
                                                                                    public void onClick(
                                                                                            DialogInterface dialog,
                                                                                            int which) {
                                                                                        PincodeActivity.this
                                                                                                .startActivity(new Intent(
                                                                                                        Settings.ACTION_WIFI_SETTINGS));
                                                                                    }
                                                                                })
                                                                        .setPositiveButton(
                                                                                "MOBILE DATA",
                                                                                new DialogInterface.OnClickListener() {

                                                                                    @Override
                                                                                    public void onClick(
                                                                                            DialogInterface dialog,
                                                                                            int which) {
                                                                                        dialog.dismiss();
                                                                                        PincodeActivity.this
                                                                                                .startActivity(new Intent(
                                                                                                        Settings.ACTION_DATA_ROAMING_SETTINGS));
                                                                                    }
                                                                                });
                                                                AlertDialog alert = alertDlg
                                                                        .create();
                                                                alert.show();
                                                            }
                                                        });
                                        AlertDialog alert = alr.create();
                                        alert.show();
                                    } else {
                                        ConstCommon.flag = false;
                                    }

                                }
                            });
            AlertDialog alert = alertDialogBuilder.create();
            alert.show();

        }
    }
}
