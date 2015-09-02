package hanelsoft.vn.timeattendance.model.entity;

import hanelsoft.vn.timeattendance.model.DAO.daoProject;
import hanelsoft.vn.timeattendance.model.helper.DBDefinition;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class ProjectEntity extends BaseEntity {

	public ProjectEntity(Context mContext) {
		super(DBDefinition.TABLE_PROJECT, mContext);
	}

	@Override
	public long add(Object obj) {
		daoProject act = (daoProject) obj;
		if (act != null) {
			ContentValues values = new ContentValues();
			values.put(DBDefinition.COLUMN__PROJECTID, act.getID());
			values.put(DBDefinition.COLUMN_PROJECT_NAME, act.getNameProject());
			values.put(DBDefinition.COLUMN_PROJECT_ALLOW_SKIP,
					act.getAllowSkip());

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
		daoProject act = (daoProject) obj;
		if (act != null) {
			return super.delete(null, null);
		} else
			return 0;
	}

	public int deleteByID(String ID) {

		if (ID != null) {
			String where = DBDefinition.COLUMN__PROJECTID + " = ?";
			String[] whereArgs = new String[] { ID };
			return super.delete(where, whereArgs);
		} else
			return 0;
	}

	@Override
	public Object getById(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	public ArrayList<daoProject> getByID(String id) {
		ArrayList<daoProject> dao = new ArrayList<daoProject>();
		try {
			if (null != _db)
				_db = null;
			_db = _dbhandler.getWritableDatabase();
			Cursor mCursor = _db.query(_tblname, null,
					DBDefinition.COLUMN__PROJECTID + " = ?",
					new String[] { id }, null, null, null);
			if (mCursor != null && mCursor.moveToFirst()) {
				daoProject objAct = new daoProject(mCursor.getString(1),
						mCursor.getString(2), mCursor.getString(5));

				dao.add(objAct);
			}
			mCursor.close();
			_db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dao;
	}

	@Override
	public ArrayList<Object> getAll() {
		return null;
	}

	public ArrayList<daoProject> getAllProject() {
		ArrayList<daoProject> listObject = new ArrayList<daoProject>();

		try {
			if (null != _db)
				_db = null;
			_db = _dbhandler.getWritableDatabase();
			Cursor mCursor = _db
					.rawQuery(
							"Select Project.projectID,projectName,LocationX,LocationY,Address,LocationID, Project.projectAllowSkip"
									+ " from Project inner join ProjectLocation"
									+ " on Project.projectID = ProjectLocation.ProjectID",
							null);
			if (mCursor != null) {
				mCursor.moveToFirst();
				do {
					daoProject dao = new daoProject(mCursor.getString(0),
							mCursor.getString(1), mCursor.getString(2),
							mCursor.getString(3), mCursor.getString(4),
							mCursor.getString(5), mCursor.getString(6));
					listObject.add(dao);
				} while (mCursor.moveToNext());
			}
			mCursor.close();
			_db.close();
		} catch (Exception e) {
			e.printStackTrace();
			listObject.clear();
			_db.close();
		}
		return listObject;
	}
}
