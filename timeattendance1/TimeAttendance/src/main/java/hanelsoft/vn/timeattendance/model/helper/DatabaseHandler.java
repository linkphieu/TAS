package hanelsoft.vn.timeattendance.model.helper;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseHandler extends SQLiteOpenHelper {
	/*----------- Class la viec truc tiep voi db thuc hien cac phuong thuc view, cap nhat dl trong db ------------*/
	// window
	private SQLiteDatabase mDataBase;
	private final Context mContext;

	public DatabaseHandler(Context context) {
		super(context, DBDefinition.DATABASE_NAME, null,
				DBDefinition.DATABASE_VERSION);
		this.mContext = context;
	}

	public void createDataBase() throws IOException {
		// If database not exists copy it from the assets

		boolean mDataBaseExist = checkDataBase();
		if (!mDataBaseExist) {
			this.getReadableDatabase();
			this.close();
			try {
				// Copy the database from assests
				copyDataBase();
			} catch (IOException mIOException) {
				throw new Error("ErrorCopyingDataBase");
			}
		}
	}

	// Check that the database exists here: /data/data/your package/databases/Da
	// Name
	private boolean checkDataBase() {
		File dbFile = new File(DBDefinition.DB_PATH
				+ DBDefinition.DATABASE_NAME);
		return dbFile.exists();
	}

	// Copy the database from assets
	private void copyDataBase() throws IOException {
		InputStream mInput = mContext.getAssets().open(
				DBDefinition.DATABASE_NAME);
		String outFileName = DBDefinition.DB_PATH + DBDefinition.DATABASE_NAME;
		OutputStream mOutput = new FileOutputStream(outFileName);
		byte[] mBuffer = new byte[1024];
		int mLength;
		while ((mLength = mInput.read(mBuffer)) > 0) {
			mOutput.write(mBuffer, 0, mLength);
		}
		mOutput.flush();
		mOutput.close();
		mInput.close();
	}
	public boolean openDataBase() throws SQLException {
		String mPath = DBDefinition.DB_PATH + DBDefinition.DATABASE_NAME;
		mDataBase = SQLiteDatabase.openDatabase(mPath, null,
				SQLiteDatabase.CREATE_IF_NECESSARY);
		// mDataBase = SQLiteDatabase.openDatabase(mPath, null,
		// SQLiteDatabase.NO_LOCALIZED_COLLATORS);
		return mDataBase != null;
	}

	@Override
	public synchronized void close() {
		if (mDataBase != null)
			mDataBase.close();
		super.close();
	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}
}
