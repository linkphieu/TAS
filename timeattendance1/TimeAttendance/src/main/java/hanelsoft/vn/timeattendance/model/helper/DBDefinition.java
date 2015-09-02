package hanelsoft.vn.timeattendance.model.helper;

import android.annotation.SuppressLint;

@SuppressLint("SdCardPath")
public class DBDefinition {
	/*------------ Class dung de dinh nghia cac ten cua db, ten bang, ten cac truong... ----------------*/
	// database version
	public static final int DATABASE_VERSION = 1;

	// database name
	public static final String DATABASE_NAME = "TAS.sqlite";
	public static final String DB_PATH = "/data/data/com.a2000.tas/databases/";
	public static final String COLUMN_ID = "id";

	// query type
	public static final String TYPE_INSERT = "insert";
	public static final String TYPE_UPDATE = "update";
	public static final String TYPE_DELETE = "delete";

	// table Project
	public static final String TABLE_PROJECT = "Project";
	public static final String COLUMN_PROJECT_ID = "ID";
	public static final String COLUMN__PROJECTID = "projectID";
	public static final String COLUMN_PROJECT_NAME = "projectName";
	public static final String COLUMN_PROJECT_ALLOW_SKIP = "projectAllowSkip";
	// table ProjectLocation
	public static final String TABLE_PROJECT_LOCATION = "ProjectLocation";
	public static final String COLUMN_LOCATION_X = "LocationX";
	public static final String COLUMN_LOCATION_Y = "LocationY";
	public static final String COLUMN_PROJECT_LOCATION_ID = "ProjectID";
	public static final String COLUMN_LOCATION_ID = "LocationID";
	public static final String COLUMN_PROJECT_ADDRESS = "Address";
	// table Timesheet
	public static final String TABLE_TIMESHEET = "Timesheet";
	public static final String COLUMN_TIMESHEET_ID = "id";
	public static final String COLUMN_EMID = "EmpID";
	public static final String COLUMN_LAT = "Lat";
	public static final String COLUMN_LNG = "Lng";
	public static final String COLUMN_IMAGE = "Image";
	public static final String COLUMN_TIME = "Time";
	public static final String COLUMN_ACTION = "Action";
	public static final String COLUMN_STATUS = "Status";
	// table DeleteEmp
	public static final String TABLE_DELEMPLOYEE = "DelEmployee";
	public static final String COLUMN_ROWID = "RowID";
	public static final String COLUMN_CREATEDATE = "CreateDate";
	// table DeleteProject
	public static final String TABLE_DELPROJECT = "DelProject";
	// table Employee
	public static final String TABLE_EMPLOYEE = "Employee";
	public static final String COLUMN_EMPID = "EmpID";
	public static final String COLUMN_NAMEEMP = "NameEmp";
	public static final String COLUMN_URLIMAGE = "urlImage";
	// table Time Sync
	public static final String TABLE_TIME_SYNC = "TimeSync";
	public static final String COLUMN_SYNC_ID = "id";
	public static final String COLUMN_TIME_SYNC = "TimeSync";
	// table UrlServer
	public static final String TABLE_URL_SERVER = "UrlServer";
	public static final String COLUMN_URL_ID = "id";
	public static final String COLUMN_URL_SERVER = "Url";

	public static final String TABLE_EMPLOYEE_CLOCK = "StatusClock";
	public static final String COLUMN_EMPLOYEE_CLOCK_ID = "EmpId";
	public static final String COLUMN_EMPLOYEE_CLOCK_STATUS = "StatusClock";
	public static final String COLUMN_EMPLOYEE_CLOCK_DATE = "DateClock";
	public static final String COLUMN_EMPLOYEE_CLOCK_PROJECT_ID = "ProjectId";
	public static final String COLUMN_EMPLOYEE_CLOCK_PROJECT_NAME = "ProjectName";
	public static final String COLUMN_EMPLOYEE_CLOCK_PROJECT_LAT = "ProjectLat";
	public static final String COLUMN_EMPLOYEE_CLOCK_PROJECT_LON = "ProjectLon";

}
