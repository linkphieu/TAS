package hanelsoft.vn.timeattendance.model.entity;

import hanelsoft.vn.timeattendance.model.DAO.daoEmp;
import hanelsoft.vn.timeattendance.model.helper.DBDefinition;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class EmployeeEntity extends BaseEntity {
	public EmployeeEntity(Context mContext) {
		super(DBDefinition.TABLE_EMPLOYEE, mContext);
	}

	@Override
	public long add(Object obj) {
		daoEmp act = (daoEmp) obj;
		if (act != null) {
			ContentValues values = new ContentValues();
			values.put(DBDefinition.COLUMN_EMPID, act.getID());
			values.put(DBDefinition.COLUMN_NAMEEMP, act.getNameEmp());

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
			String where = "EmpID = ?";
			String[] whereArgs = new String[] { ID };
			return super.delete(where, whereArgs);
		} else
			return 0;
	}

	@Override
	public Object getById(String id) {
		try {
			if (null != _db)
				_db = null;
			_db = _dbhandler.getWritableDatabase();
			Cursor mCursor = _db.query(_tblname, null,
					DBDefinition.COLUMN_EMPID + " = ?", new String[] { id },
					null, null, null);
			if (mCursor != null && mCursor.moveToFirst()) {
				daoEmp objAct = new daoEmp(mCursor.getString(0),
						mCursor.getString(1));
				return objAct;
			}
			mCursor.close();
			_db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public ArrayList<daoEmp> getByID(String id) {
		ArrayList<daoEmp> arr = new ArrayList<daoEmp>();
		try {
			if (null != _db)
				_db = null;
			_db = _dbhandler.getWritableDatabase();
			Cursor mCursor = _db.query(_tblname, null,
					DBDefinition.COLUMN_EMPID + " = ?", new String[] { id },
					null, null, null);
			if (mCursor != null && mCursor.moveToFirst()) {
				daoEmp objAct = new daoEmp(mCursor.getString(0),
						mCursor.getString(1));
				arr.add(objAct);
			}
			mCursor.close();
			_db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return arr;
	}

	@Override
	public ArrayList<Object> getAll() {
		// TODO Auto-generated method stub
		return null;
	}
}
