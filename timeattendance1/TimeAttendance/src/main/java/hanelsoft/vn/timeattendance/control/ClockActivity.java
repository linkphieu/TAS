package hanelsoft.vn.timeattendance.control;

import android.R.drawable;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.a2000.tas.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import hanelsoft.vn.timeattendance.common.ConstCommon;
import hanelsoft.vn.timeattendance.common.UtilsCommon;
import hanelsoft.vn.timeattendance.model.DAO.adapterProject;
import hanelsoft.vn.timeattendance.model.DAO.daoClock;
import hanelsoft.vn.timeattendance.model.DAO.daoEmp;
import hanelsoft.vn.timeattendance.model.DAO.daoEmpClock;
import hanelsoft.vn.timeattendance.model.DAO.daoProject;
import hanelsoft.vn.timeattendance.model.entity.EmployeeClockEntity;
import hanelsoft.vn.timeattendance.model.entity.EmployeeEntity;
import hanelsoft.vn.timeattendance.model.entity.ProjectEntity;
import hanelsoft.vn.timeattendance.model.entity.TimeSyncEntity;
import hanelsoft.vn.timeattendance.model.entity.TimesheetEntity;
import hanelsoft.vn.timeattendance.model.helper.FileHelper;
import hanelsoft.vn.timeattendance.service.TimerService;

@SuppressLint("DefaultLocale")
public class ClockActivity extends Activity {
    ImageView img;
    String encodeImage;
    GoogleMap googleMap;
    TextView nameEmp;
    ArrayList<daoProject> arrayList = new ArrayList<daoProject>();
    ArrayList<daoProject> arrayListResult = new ArrayList<daoProject>();
    AlertDialog alertDialog;
    ProgressDialog progressDialog = null;
    AlertDialog.Builder alertDialogBuilder;
    private daoProject mDaoProject;
    private adapterProject mAdapterProject;

    ProjectEntity projectEntity;
    EmployeeEntity employeeEntity;
    EmployeeClockEntity employeeClockEntity;
    TimeSyncEntity timesyncEntity;
    boolean isHaveProject = false;
    private String str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock);
        projectEntity = new ProjectEntity(this);
        employeeEntity = new EmployeeEntity(this);
        timesyncEntity = new TimeSyncEntity(this);
        employeeClockEntity = new EmployeeClockEntity(this);
        nameEmp = (TextView) findViewById(R.id.NameEmployee);
        initilizeMap();
        ArrayList<daoEmp> arr = employeeEntity.getByID(ConstCommon.EmID);
        if (arr.size() > 0) {
            String nameEmployee = arr.get(0).getNameEmp();
            nameEmp.setText(nameEmployee);
        }
        Intent intent = getIntent();
        ConstCommon.pathImage = intent.getStringExtra("pathImage");
        ConstCommon.action = intent.getIntExtra("ACTION", 0);
        img = (ImageView) findViewById(R.id.imageView1);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 12;
        Bitmap bitmap;
        if (ConstCommon.bitMapImageWhenSkip != null) {
            bitmap = ConstCommon.bitMapImageWhenSkip;
            ConstCommon.bitMapImageWhenSkip = null;
        } else {
            bitmap = BitmapFactory.decodeFile(ConstCommon.pathImage);
        }
        //
        // bitmap = ImageHelper.scaleImage(bitmap, 120);
        img.setImageBitmap(bitmap);

        int status = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(getBaseContext());
        try {
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setZoomControlsEnabled(false);
            googleMap.getUiSettings().setCompassEnabled(true);
            googleMap.getUiSettings().setRotateGesturesEnabled(true);
            googleMap.getUiSettings().setZoomGesturesEnabled(true);
            if (status != ConnectionResult.SUCCESS) {
                int requestCode = 10;
                Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status,
                        this, requestCode);
                dialog.show();
            } else {
                LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                Criteria criteria = new Criteria();
                String provider = locationManager.getBestProvider(criteria, true);
                locationManager.requestLocationUpdates(provider, 1 * 1000, 0,
                        new LocationListener() {
                            @Override
                            public void onStatusChanged(String provider,
                                                        int status, Bundle extras) {
                            }

                            @Override
                            public void onProviderEnabled(String provider) {
                            }

                            @Override
                            public void onProviderDisabled(String provider) {
                            }

                            @Override
                            public void onLocationChanged(Location location) {
                                ConstCommon.latLng = new LatLng(location
                                        .getLatitude(), location.getLatitude());

                            }
                        });
                Location location = locationManager
                        .getLastKnownLocation(provider);
                if (location != null) {
                    ConstCommon.latitude = location.getLatitude();
                    ConstCommon.longitude = location.getLongitude();
                    ConstCommon.latLng = new LatLng(ConstCommon.latitude,
                            ConstCommon.longitude);
                    MarkerOptions marker = new MarkerOptions().position(
                            ConstCommon.latLng).title("My Location");
                    googleMap.addMarker(marker);
                    googleMap.moveCamera(CameraUpdateFactory
                            .newLatLng(ConstCommon.latLng));
                    googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            showGPSDisabledAlertToUser();
        } else {
            Location locationGPS = locationManager
                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location locationNet = locationManager
                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            long GPSLocationTime = 0;
            if (null != locationGPS) {
                GPSLocationTime = locationGPS.getTime();
            }

            long NetLocationTime = 0;

            if (null != locationNet) {
                NetLocationTime = locationNet.getTime();
            }

            if (null != locationNet && null != locationGPS) {
                if (0 < GPSLocationTime - NetLocationTime) {
                    ConstCommon.latitude = locationGPS.getLatitude();
                    ConstCommon.longitude = locationGPS.getLongitude();
                    ConstCommon.latLng = new LatLng(ConstCommon.latitude,
                            ConstCommon.longitude);
                } else {
                    ConstCommon.latitude = locationNet.getLatitude();
                    ConstCommon.longitude = locationNet.getLongitude();
                    ConstCommon.latLng = new LatLng(ConstCommon.latitude,
                            ConstCommon.longitude);
                }
            }

            try {
                MarkerOptions marker = new MarkerOptions().position(
                        ConstCommon.latLng).title("My Location");
                googleMap.addMarker(marker);
                googleMap.moveCamera(CameraUpdateFactory
                        .newLatLng(ConstCommon.latLng));
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
            } catch (Exception e) {

            }

            ShowDialogProject();
        }
        super.onResume();
    }

    @SuppressLint("DefaultLocale")
    private boolean checkIsCheckOutProject(Context context,
                                           daoProject _mDaoProject) {
        daoEmpClock _daoEmpClock = new daoEmpClock();
        _daoEmpClock = employeeClockEntity
                .getStatusByID(ConstCommon.daoEmpWhenPinCode.getID());
        if (_daoEmpClock.getProjectId().toUpperCase()
                .equals(_mDaoProject.getID().toUpperCase()) == true
                && _daoEmpClock.getProjectLat().toUpperCase()
                .equals(_mDaoProject.getLocationX().toUpperCase()) == true
                && _daoEmpClock.getProjectLon().toUpperCase()
                .equals(_mDaoProject.getLocationY().toUpperCase()) == true) {
            return true;
        }
        return false;
    }

    @SuppressLint("DefaultLocale")
    private void ShowDialogProject() {
        LayoutInflater li = LayoutInflater.from(ClockActivity.this);
        View promptsView = li.inflate(R.layout.dialog_project, null);
        alertDialogBuilder = new AlertDialog.Builder(ClockActivity.this);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.setTitle("Available projects:");
        ArrayList<daoEmp> arr = new ArrayList<daoEmp>();
        arr = employeeEntity.getByID(ConstCommon.EmID);
        System.out.println(ConstCommon.EmID);
        if ((ConstCommon.latitude == 0) && (ConstCommon.longitude == 0)) {
            Toast.makeText(
                    ClockActivity.this,
                    "Can't get your current location! Please click locate button to get your location.",
                    Toast.LENGTH_SHORT).show();

        } else {
            arrayListResult.clear();
            arrayList.clear();
            arrayList.addAll(projectEntity.getAllProject());
            for (int i = 0; i < arrayList.size(); i++) {

                daoProject pro = arrayList.get(i);
                if (pro.getAllowSkip().toUpperCase().equals("TRUE") == true) {
                    arrayListResult.add(pro);
                    isHaveProject = true;
                } else {
                    double result = UtilsCommon.CalculationByDistance(
                            ConstCommon.latitude, ConstCommon.longitude,
                            pro.getLocationX(), pro.getLocationY());
                    if (result < 0.5) {
                        arrayListResult.add(pro);
                        isHaveProject = true;
                    }
                }
            }
            if (isHaveProject) {
                try {
                    alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                } catch (Exception e) {
                    // TODO: handle exception
                }
            } else {
                try {
                    AlertDialog.Builder alertDlg = new AlertDialog.Builder(
                            ClockActivity.this);
                    alertDlg.setMessage(
                            "There is no project at this location. Please try again!")
                            .setIcon(drawable.ic_dialog_alert)
                            .setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(
                                                DialogInterface dialog,
                                                int which) {
                                            setFinish();
                                        }
                                    });
                    alertDlg.show();
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
            final ListView mListView = (ListView) promptsView
                    .findViewById(R.id.listData);
            final Spinner mSpinner = (Spinner) promptsView
                    .findViewById(R.id.spinner1);
            mSpinner.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
            mAdapterProject = new adapterProject(ClockActivity.this,
                    arrayListResult);

            mSpinner.setAdapter(mAdapterProject);
            mListView.setAdapter(mAdapterProject);
            mListView.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    if (mListView.getSelectedItemPosition() != 0) {
                        ConstCommon.ProjectID = "";
                        mDaoProject = arrayListResult.get(position);
                        ConstCommon.ProjectID = mDaoProject.getID();
                        ConstCommon.ProjectLocationID = mDaoProject
                                .getProjectLocationID();
                        System.out.println("Project ID:"
                                + ConstCommon.ProjectID + "LocationID: "
                                + ConstCommon.ProjectLocationID);

                        alertDialog.dismiss();

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                ClockActivity.this);
//                        TextView title = new TextView(ClockActivity.this);
//                        title.setGravity(Gravity.CENTER);
//                        title.setText("Are you sure to clock (Yes/No)?");

                        alertDialogBuilder
                                .setTitle("Warning!")
                                .setMessage("Are you sure to clock (Yes/No)?")
                                .setCancelable(false)
                                .setPositiveButton("Yes",
                                        new OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                callDoClock();
                                            }
                                        })
                                .setNegativeButton("No", new OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // TODO Auto-generated method stub

                                    }
                                });
                        AlertDialog alert = alertDialogBuilder.create();
                        alert.show();
                    }
                }

            });

            // show it
            mSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int position, long id) {
                    if (mSpinner.getSelectedItemPosition() != 0) {
                        ConstCommon.ProjectID = "";
                        mDaoProject = arrayListResult.get(position);
                        ConstCommon.ProjectID = mDaoProject.getID();
                        ConstCommon.ProjectLocationID = mDaoProject
                                .getProjectLocationID();
                        System.out.println("Project ID:"
                                + ConstCommon.ProjectID + "LocationID: "
                                + ConstCommon.ProjectLocationID);
                        alertDialog.dismiss();
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                ClockActivity.this);
                        TextView title = new TextView(ClockActivity.this);
                        title.setGravity(Gravity.CENTER);
                        title.setText("Are you sure?");
                        alertDialogBuilder
                                .setCustomTitle(title)
                                .setCancelable(false)
                                .setPositiveButton("Yes",
                                        new OnClickListener() {

                                            @Override
                                            public void onClick(
                                                    DialogInterface dialog,
                                                    int which) {
                                                callDoClock();
                                            }
                                        })
                                .setNegativeButton("No", new OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        // TODO Auto-generated method stub

                                    }
                                });
                        AlertDialog alert = alertDialogBuilder.create();
                        alert.show();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            ConstCommon.nameEmp = arr.get(0).getNameEmp();

        }
    }

    private void initilizeMap() {
        if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.fragment1)).getMap();
            try {
                googleMap
                        .setOnMyLocationButtonClickListener(new OnMyLocationButtonClickListener() {

                            @Override
                            public boolean onMyLocationButtonClick() {
                                try {
                                    ConstCommon.longitude = googleMap
                                            .getMyLocation().getLongitude();
                                    ConstCommon.latitude = googleMap
                                            .getMyLocation().getLatitude();
                                    ShowDialogProject();
                                } catch (Exception e) {
                                    e.printStackTrace();

                                }
                                return false;
                            }
                        });
            } catch (Exception e) {
                // TODO: handle exception
            }
            if (googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    private void showGPSDisabledAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder
                .setMessage("GPS is not enable")
                .setCancelable(false)
                .setPositiveButton("Setting",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
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
            so.addProperty("LocationID", ConstCommon.ProjectLocationID);
            SoapSerializationEnvelope sse = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            new MarshalBase64().register(sse);
            sse.dotNet = true;
            sse.setOutputSoapObject(so);
            HttpTransportSE htse = new HttpTransportSE(FileHelper.readFromFile(this));
            htse.call(ConstCommon.SOAP_ACTION_UPLOAD, sse);
            SoapPrimitive response = (SoapPrimitive) sse.getResponse();
            str = response.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callDoClock() {
        doClock();
    }

    private void doClock() {
        if (UtilsCommon.haveNetworkConnection(ClockActivity.this)) {
            try {
                encodeImage = UtilsCommon.encodeImage(ConstCommon.pathImage);
            } catch (Exception e) {
                // TODO: handle
                // exception
            }
            progressDialog = ProgressDialog.show(ClockActivity.this, "",
                    "Please wait...", true);
            AsyncCallWS task = new AsyncCallWS();
            task.execute();
        } else {
            ArrayList<daoProject> arrProj = new ArrayList<daoProject>();
            ProjectEntity projectEnt = new ProjectEntity(ClockActivity.this);
            arrProj = projectEnt.getByID(ConstCommon.ProjectID);
            daoClock dao;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentDateandTime = sdf.format(new Date());
            TimesheetEntity entity = new TimesheetEntity(ClockActivity.this);
            dao = entity.getLastClockByEmID(Integer.parseInt(ConstCommon.EmID));
            if (dao.getAction().equals("1")) {
                dao.setAction("2");
                entity.add(dao);
            }
            dao.setEmployeeID(Integer.parseInt(ConstCommon.EmID));
            dao.setLat(ConstCommon.latitude);
            dao.setLng(ConstCommon.longitude);
            dao.setImage(ConstCommon.pathImage);
            dao.setAction(String.valueOf(ConstCommon.action));
            dao.setStatus(0);
            dao.setDatetime(currentDateandTime);
            dao.setProjectID(ConstCommon.ProjectID);
            dao.setLocationID(ConstCommon.ProjectLocationID);
            entity.add(dao);
            String nameAction = "";
            if (ConstCommon.action == 1) {
                nameAction = "Clock in successfully!";
                daoEmpClock _dao = new daoEmpClock(
                        ConstCommon.daoEmpWhenPinCode.getID(),
                        ConstCommon.STR_CLOCK_IN, "", dao.getProjectID(),
                        mDaoProject.getNameProject(),
                        mDaoProject.getLocationX(), mDaoProject.getLocationY());
                employeeClockEntity.add(_dao);
            } else if (ConstCommon.action == 2) {
                nameAction = "Out for lunch is done! Thank you! Have a nice day!";
            } else if (ConstCommon.action == 3) {
                nameAction = "Back from lunch is done! Thank you! Have a nice day!";
            } else if (ConstCommon.action == 4) {
                nameAction = "Clock out successfully!";
                daoEmpClock _dao = new daoEmpClock(
                        ConstCommon.daoEmpWhenPinCode.getID(),
                        ConstCommon.STR_CLOCK_OUT, "", dao.getProjectID(),
                        mDaoProject.getNameProject(),
                        mDaoProject.getLocationX(), mDaoProject.getLocationY());
                employeeClockEntity.add(_dao);
            }

            final Dialog dialogInformation = new Dialog(ClockActivity.this);
            TextView txtNameEmp, txtAction, txtTime;
            ImageView image;
            Button btnOK;
            dialogInformation
                    .setContentView(com.a2000.tas.R.layout.message_clock_success);
            dialogInformation.setTitle("Clocking Result");
            dialogInformation.setCancelable(false);
            txtNameEmp = (TextView) dialogInformation
                    .findViewById(com.a2000.tas.R.id.txtNameEmp);
            txtTime = (TextView) dialogInformation
                    .findViewById(com.a2000.tas.R.id.txtTime);
            txtAction = (TextView) dialogInformation
                    .findViewById(com.a2000.tas.R.id.txtAction);
            btnOK = (Button) dialogInformation
                    .findViewById(com.a2000.tas.R.id.btnOK);
            image = (ImageView) dialogInformation
                    .findViewById(com.a2000.tas.R.id.image);
            BitmapFactory.Options options = new BitmapFactory.Options();
            // options.inSampleSize = 5;
            final Bitmap bitmap = BitmapFactory.decodeFile(
                    ConstCommon.pathImage, options);
            txtNameEmp.setText(ConstCommon.nameEmp);
            txtAction.setText(nameAction);
            txtTime.setText(currentDateandTime);
            dialogInformation.show();
            btnOK.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (UtilsCommon.isClickForResult) {
                        UtilsCommon.isCheckedForResult = true;
                    }
                    setFinish();
                }
            });
        }
    }
    void setFinish() {
        ConstCommon.PROJECT_IS_SKIP = false;
        ConstCommon.PROJECT_SKIP = "FALSE";
        Intent serviceIntent = new Intent(this, TimerService.class);
        startService(serviceIntent);
        ClockActivity.this.finish();
        Intent pincode = new Intent(this, PincodeActivity.class);
        startActivity(pincode);
    }

    @Override
    public void onBackPressed() {
        setFinish();
    }

    private class AsyncCallWS extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            try {
                getProject(encodeImage);
            } catch (Exception e) {
                progressDialog.dismiss();
                AlertDialog.Builder alertDlg = new AlertDialog.Builder(ClockActivity.this);
                alertDlg.setMessage("Can't connect to server")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                setFinish();
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
                String success = obj.getString("Status");
                if (success.equals("1")) {
                    progressDialog.dismiss();
                    AlertDialog.Builder alertDlg = new AlertDialog.Builder(ClockActivity.this);
                    TextView title = new TextView(ClockActivity.this);
                    String nameAction = "";
                    if (ConstCommon.action == 1) {
                        nameAction = "Clock in successfully!";
                        daoEmpClock _daoEmpClock = employeeClockEntity.getStatusByID(ConstCommon.daoEmpWhenPinCode.getID());
                        if (_daoEmpClock.getStatusClock() != null && _daoEmpClock.getStatusClock().length() > 0) {
                            _daoEmpClock.setStatusClock(ConstCommon.STR_CLOCK_OUT);
                            employeeClockEntity.add(_daoEmpClock);
                        }
                        daoEmpClock _dao = new daoEmpClock(
                                ConstCommon.daoEmpWhenPinCode.getID(),
                                ConstCommon.STR_CLOCK_IN, "",
                                mDaoProject.getID(),
                                mDaoProject.getNameProject(),
                                mDaoProject.getLocationX(),
                                mDaoProject.getLocationY());
                        employeeClockEntity.add(_dao);

                    } else if (ConstCommon.action == 2) {
                        nameAction = "Out for lunch is done! Thank you! Have a nice day!";
                    } else if (ConstCommon.action == 3) {
                        nameAction = "Back from lunch is done! Thank you! Have a nice day!";
                    } else if (ConstCommon.action == 4) {
                        nameAction = "Clock out successfully!";
                        daoEmpClock _dao = new daoEmpClock(
                                ConstCommon.daoEmpWhenPinCode.getID(),
                                ConstCommon.STR_CLOCK_OUT, "",
                                mDaoProject.getID(),
                                mDaoProject.getNameProject(),
                                mDaoProject.getLocationX(),
                                mDaoProject.getLocationY());
                        employeeClockEntity.add(_dao);
                    }
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String currentDateandTime = sdf.format(new Date());
                    final Dialog dialogInformation = new Dialog(
                            ClockActivity.this);
                    TextView txtNameEmp, txtAction, txtTime;
                    ImageView image;
                    Button btnOK;
                    dialogInformation
                            .setContentView(com.a2000.tas.R.layout.message_clock_success);
                    dialogInformation.setTitle("Clocking result");
                    dialogInformation.setCancelable(false);
                    txtNameEmp = (TextView) dialogInformation
                            .findViewById(com.a2000.tas.R.id.txtNameEmp);
                    txtTime = (TextView) dialogInformation
                            .findViewById(com.a2000.tas.R.id.txtTime);
                    txtAction = (TextView) dialogInformation
                            .findViewById(com.a2000.tas.R.id.txtAction);
                    btnOK = (Button) dialogInformation
                            .findViewById(com.a2000.tas.R.id.btnOK);

                    image = (ImageView) dialogInformation
                            .findViewById(com.a2000.tas.R.id.image);
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 1;
                    final Bitmap bitmap = BitmapFactory.decodeFile(
                            ConstCommon.pathImage, options);
                    image.setImageBitmap(bitmap);
                    txtNameEmp.setText(ConstCommon.nameEmp);
                    txtAction.setText(nameAction);
                    txtTime.setText(currentDateandTime);
                    btnOK.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            if (UtilsCommon.isClickForResult) {
                                UtilsCommon.isCheckedForResult = true;
                            }
                            setFinish();
                        }
                    });
                    try {
                        dialogInformation.show();
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                } else {
                    progressDialog.dismiss();
                    AlertDialog.Builder alertDlg = new AlertDialog.Builder(
                            ClockActivity.this);
                    alertDlg.setMessage(obj.getString("Message"))

                            .setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            if (UtilsCommon.isClickForResult) {
                                                UtilsCommon.isCheckedForResult = true;
                                            }
                                            setFinish();
                                        }
                                    });

                    alertDlg.show();
                }
            } catch (Exception e) {
            }

        }
    }
}
