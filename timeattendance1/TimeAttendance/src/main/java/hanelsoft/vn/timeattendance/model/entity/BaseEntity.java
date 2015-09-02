package hanelsoft.vn.timeattendance.model.entity;

import hanelsoft.vn.timeattendance.model.helper.DatabaseHandler;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public abstract class BaseEntity {
	public DatabaseHandler _dbhandler;
	protected SQLiteDatabase _db;
	protected Context _context;
	protected String _tblname;

	public BaseEntity(String tblName, Context mContext) {
		this._tblname = tblName;
		this._dbhandler = new DatabaseHandler(mContext);
		this._context = mContext;
	}

	public abstract long add(Object obj);

	/**
	 * insert ban ghi vao CSDL
	 * 
	 * @param values
	 * @return id cua ban ghi moi duoc them (-1 neu ko them duoc)
	 */
	public long add(ContentValues values) {
		long id = -1;
		// try {
		if (null != _db)
			_db = null;
		_db = _dbhandler.getWritableDatabase();
		id = _db.insert(_tblname, null, values);
		_db.close();
		return id;
		// } catch (Exception e) {
		// return id;
		// }
	}

	public abstract boolean update(Object obj);

	/**
	 * update ban ghi
	 * 
	 * @param values
	 * @param where
	 * @return
	 */
	public boolean update(ContentValues values, String where, String[] whereArgs) {
		try {
			if (null != _db)
				_db = null;
			_db = _dbhandler.getWritableDatabase();
			int row = _db.update(_tblname, values, where, whereArgs);
			_db.close();
			if (row > 0)
				return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public abstract int delete(Object obj);

	/**
	 * xoa ban ghi trong co so du lieu
	 * 
	 * @param where
	 *            : dieu kien xoa
	 * @return so ban ghi da xoa (0 neu ko xoa duoc)
	 */
	public int delete(String where, String[] whereArgs) {
		int count = 0;
		try {
			if (null != _db)
				_db = null;
			_db = _dbhandler.getWritableDatabase();
			count = _db.delete(_tblname, where, whereArgs);
			_db.close();
			return count;
		} catch (Exception e) {
			return count;
		}
	}

	public abstract Object getById(String id);

	public abstract ArrayList<Object> getAll();

	public ArrayList<Object> search() {
		return new ArrayList<Object>();
	}

}