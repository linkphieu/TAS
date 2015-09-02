package hanelsoft.vn.timeattendance.model.entity;

import hanelsoft.vn.timeattendance.model.DAO.daoTimeSync;
import hanelsoft.vn.timeattendance.model.helper.DBDefinition;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class TimeSyncEntity extends BaseEntity {
	public TimeSyncEntity(Context mContext) {
		super(DBDefinition.TABLE_TIME_SYNC, mContext);
	}

	@Override
	public long add(Object obj) {
		daoTimeSync act = (daoTimeSync) obj;
		if (act != null) {
			ContentValues values = new ContentValues();
			values.put(DBDefinition.COLUMN_TIME_SYNC, act.getTimeSync());
			return super.add(values);
		} else {
			return 0;
		}
	}

	@Override
	public boolean update(Object obj) {

		daoTimeSync mTimeSync = (daoTimeSync) obj;
		if (mTimeSync != null) {
			ContentValues values = new ContentValues();
			values.put(DBDefinition.COLUMN_TIME_SYNC, mTimeSync.getTimeSync());
			String where = null;
			String[] whereArgs = new String[] {};
			return super.update(values, where, whereArgs);
		} else {
			return false;
		}

	}

	public boolean updateTimeSync(String strValues) {
		if (strValues != null) {
			ContentValues values = new ContentValues();
			values.put(DBDefinition.COLUMN_TIME_SYNC, strValues);
			String where = null;
			String[] whereArgs = null;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Object> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	public ArrayList<daoTimeSync> getTime() {
		ArrayList<daoTimeSync> arr = new ArrayList<daoTimeSync>();
		try {
			if (null != _db)
				_db = null;
			_db = _dbhandler.getWritableDatabase();
			Cursor mCursor = _db.query(_tblname, null, null, null, null, null,
					null);
			if (mCursor != null) {
				mCursor.moveToFirst();
				do {
					daoTimeSync objAct = new daoTimeSync(mCursor.getString(1));
					arr.add(objAct);
				} while (mCursor.moveToNext());
			}
			mCursor.close();
			_db.close();
		} catch (Exception e) {
			e.printStackTrace();
			arr.clear();
		}
		return arr;
	}

}
