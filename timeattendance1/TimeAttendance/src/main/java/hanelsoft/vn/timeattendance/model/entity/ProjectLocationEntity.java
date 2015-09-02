package hanelsoft.vn.timeattendance.model.entity;

import hanelsoft.vn.timeattendance.model.DAO.DaoProjectLocation;
import hanelsoft.vn.timeattendance.model.DAO.daoProject;
import hanelsoft.vn.timeattendance.model.helper.DBDefinition;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class ProjectLocationEntity extends BaseEntity {

	public ProjectLocationEntity(Context mContext) {
		super(DBDefinition.TABLE_PROJECT_LOCATION, mContext);
		// TODO Auto-generated constructor stub
	}

	@Override
	public long add(Object obj) {
		DaoProjectLocation act = (DaoProjectLocation) obj;
		if (act != null) {
			ContentValues values = new ContentValues();
			values.put(DBDefinition.COLUMN_PROJECT_LOCATION_ID,
					act.getProjectID());
			values.put(DBDefinition.COLUMN_LOCATION_X, act.getLocationX());
			values.put(DBDefinition.COLUMN_LOCATION_Y, act.getLocationY());
			values.put(DBDefinition.COLUMN_LOCATION_ID, act.getLocationID());
			values.put(DBDefinition.COLUMN_PROJECT_ADDRESS, act.getAddress());
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

	public ArrayList<DaoProjectLocation> getAllProjectLocation() {
		ArrayList<DaoProjectLocation> listObject = new ArrayList<DaoProjectLocation>();

		try {
			if (null != _db)
				_db = null;
			_db = _dbhandler.getWritableDatabase();
			Cursor mCursor = _db.query(_tblname, new String[] { "ProjectID",
					"LocationX", "LocaitonY", "LocationID", "Address" }, null,
					null, null, null, null);
			if (mCursor != null) {
				mCursor.moveToFirst();
				do {
					DaoProjectLocation dao = new DaoProjectLocation(
							mCursor.getString(1), mCursor.getString(2),
							mCursor.getString(3), mCursor.getString(4),
							mCursor.getString(5), "");
					listObject.add(dao);
				} while (mCursor.moveToNext());
			}
			mCursor.close();
			_db.close();
		} catch (Exception e) {
			listObject.clear();
			_db.close();
		}
		return listObject;
	}

}
