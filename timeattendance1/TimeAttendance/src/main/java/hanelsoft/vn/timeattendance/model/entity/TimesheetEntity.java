package hanelsoft.vn.timeattendance.model.entity;

import hanelsoft.vn.timeattendance.model.DAO.daoClock;
import hanelsoft.vn.timeattendance.model.helper.DBDefinition;

import java.util.ArrayList;

import android.R.integer;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class TimesheetEntity extends BaseEntity {
	public TimesheetEntity(Context mContext) {
		super(DBDefinition.TABLE_TIMESHEET, mContext);
	}

	@Override
	public long add(Object obj) {
		daoClock act = (daoClock) obj;
		if (act != null) {
			ContentValues values = new ContentValues();
			values.put(DBDefinition.COLUMN_EMID, act.getEmployeeID());
			values.put(DBDefinition.COLUMN_LAT, act.getLat());
			values.put(DBDefinition.COLUMN_LNG, act.getLng());
			values.put(DBDefinition.COLUMN_IMAGE, act.getImage());
			values.put(DBDefinition.COLUMN_TIME, act.getDatetime());
			values.put(DBDefinition.COLUMN_ACTION, act.getAction());
			values.put(DBDefinition.COLUMN_STATUS, act.getStatus());
			values.put(DBDefinition.COLUMN__PROJECTID, act.getProjectID());
			values.put(DBDefinition.COLUMN_LOCATION_ID, act.getLocationID());
			return super.add(values);
		} else {
			return 0;
		}
	}

	@Override
	public boolean update(Object obj) {
		// TODO Auto-generated method stub

		return false;
	}

	public boolean updateAttachPath(String strValues) {
		if (strValues != null) {
			ContentValues values = new ContentValues();
			values.put(DBDefinition.COLUMN_STATUS, strValues);
			String where = DBDefinition.COLUMN_STATUS + "=?";
			String[] whereArgs = new String[] { "0" };
			return super.update(values, where, whereArgs);
		} else {
			return false;
		}
	}

	@Override
	public int delete(Object obj) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getById(String id) {
		return null;
	}

	public ArrayList<daoClock> getByStatus(int status) {
		ArrayList<daoClock> listObject = new ArrayList<daoClock>();
		try {
			if (null != _db)
				_db = null;
			_db = _dbhandler.getWritableDatabase();
			Cursor mCursor = _db.query(_tblname, null,
					DBDefinition.COLUMN_STATUS + " = ?",
					new String[] { String.valueOf(status) }, null, null, null);
			if (mCursor != null) {
				mCursor.moveToFirst();
				do {
					daoClock objAct = new daoClock(Integer.parseInt(mCursor
							.getString(1)), Double.parseDouble(mCursor
							.getString(2)), Double.parseDouble(mCursor
							.getString(3)), mCursor.getString(4),
							mCursor.getString(5), mCursor.getString(6),
							Integer.parseInt(mCursor.getString(7)),
							mCursor.getString(8), mCursor.getString(9));
					listObject.add(objAct);
				} while (mCursor.moveToNext());
			}
			mCursor.close();
			_db.close();
		} catch (Exception e) {
			e.printStackTrace();
			listObject.clear();
		}
		return listObject;
	}

	@Override
	public ArrayList<Object> getAll() {
		ArrayList<Object> listObject = new ArrayList<Object>();
		try {
			if (null != _db)
				_db = null;
			_db = _dbhandler.getWritableDatabase();
			Cursor mCursor = _db.query(_tblname, new String[] { "EmpID", "Lat",
					"Lng", "Image", "Time", "Action", "Status", "projectID",
					"LocationID" }, null, null, null, null, null);
			if (mCursor != null) {
				mCursor.moveToFirst();
				do {
					listObject.add(mCursor.getString(0));
				} while (mCursor.moveToNext());
			}
			mCursor.close();
			_db.close();
		} catch (Exception e) {
			listObject.clear();
		}
		return listObject;
	}
}
