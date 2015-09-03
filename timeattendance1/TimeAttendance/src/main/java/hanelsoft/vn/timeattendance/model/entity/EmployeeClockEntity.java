package hanelsoft.vn.timeattendance.model.entity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

import hanelsoft.vn.timeattendance.common.UtilsCommon;
import hanelsoft.vn.timeattendance.model.DAO.daoEmpClock;
import hanelsoft.vn.timeattendance.model.helper.DBDefinition;

@SuppressLint("DefaultLocale")
public class EmployeeClockEntity extends BaseEntity {
	public EmployeeClockEntity(Context mContext) {
		super(DBDefinition.TABLE_EMPLOYEE_CLOCK, mContext);
	}

	@Override
	public long add(Object obj) {
		daoEmpClock act = (daoEmpClock) obj;
		if (act != null) {
			deleteByID(act.getId());

			ContentValues values = new ContentValues();
			values.put(DBDefinition.COLUMN_EMPLOYEE_CLOCK_ID, act.getId());
			values.put(DBDefinition.COLUMN_EMPLOYEE_CLOCK_STATUS,
					act.getStatusClock());
			values.put(DBDefinition.COLUMN_EMPLOYEE_CLOCK_DATE,
					UtilsCommon.getCurrentDate());
			values.put(DBDefinition.COLUMN_EMPLOYEE_CLOCK_PROJECT_ID,
					act.getProjectId());
			values.put(DBDefinition.COLUMN_EMPLOYEE_CLOCK_PROJECT_NAME,
					act.getProjectName());
			values.put(DBDefinition.COLUMN_EMPLOYEE_CLOCK_PROJECT_LAT,
					act.getProjectLat());
			values.put(DBDefinition.COLUMN_EMPLOYEE_CLOCK_PROJECT_LON,
					act.getProjectLon());

			return super.add(values);
		} else {
			return 0;
		}
	}

	@Override
	public boolean update(Object obj) {
		return false;
	}

	@Override
	public int delete(Object obj) {
		return 0;
	}

	public int deleteByID(String ID) {
		if (ID != null) {
			String where = "EmpId = ?";
			String[] whereArgs = new String[] { ID };
			return super.delete(where, whereArgs);
		} else
			return 0;
	}

	@Override
	public Object getById(String id) {

		return null;
	}

	@SuppressLint("DefaultLocale")
	public daoEmpClock getStatusByID(String id) {
		daoEmpClock _daoEmpClock = new daoEmpClock();
		try {
			if (null != _db)
				_db = null;
			_db = _dbhandler.getWritableDatabase();
			Cursor mCursor = _db.query(DBDefinition.TABLE_EMPLOYEE_CLOCK, null,
					DBDefinition.COLUMN_EMPLOYEE_CLOCK_ID + " = ?",
					new String[] { id }, null, null, null);
			if (mCursor != null && mCursor.moveToFirst()) {
				_daoEmpClock = new daoEmpClock(mCursor.getString(0)
						.toUpperCase(), mCursor.getString(1).toUpperCase(),
						mCursor.getString(2).toUpperCase(), mCursor
								.getString(3).toUpperCase(), mCursor.getString(
								4).toUpperCase(), mCursor.getString(5)
								.toUpperCase(), mCursor.getString(6)
								.toUpperCase());
			}
			mCursor.close();
			_db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return _daoEmpClock;
	}

	@Override
	public ArrayList<Object> getAll() {
		// TODO Auto-generated method stub
		return null;
	}
}
