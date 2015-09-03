package hanelsoft.vn.timeattendance.linkstech.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Link Larkin on 7/4/2015.
 */
public class SQLiteHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "frogringtone";
    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE IF NOT EXISTS " + DATABASE_NAME + "(ID INTEGER PRIMARY KEY,ICON TEXT, TITLE TEXT, DESCRIPTION TEXT,REQUEST TEXT)";


    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL(SQL_DELETE_ENTRIES);
        //onCreate(db);
    }

//    public void addGiftItems(List<GiftItem> list) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        for (GiftItem item : list) {
//            ContentValues values = new ContentValues();
//            values.put("ICON", item.getIcon());
//            values.put("TITLE", item.getTitle());
//            values.put("DESCRIPTION", item.getDescription());
//            values.put("REQUEST", item.getAction().getRequest());
//            db.insert(DATABASE_NAME, null, values);
//        }
//        db.close();
//    }
//
//    public List<GiftItem> getGiftItems() {
//        List<GiftItem> items = new ArrayList<>();
//        Cursor cursor = this.getWritableDatabase().rawQuery("SELECT * FROM " + DATABASE_NAME, null);
//        if (cursor.moveToFirst()) {
//            do {
//                items.add(new GiftItem(cursor.getString(1), cursor.getString(2), cursor.getString(3), new GiftItem.Action(cursor.getString(4))));
//            }
//            while (cursor.moveToNext());
//        }
//        return items;
//    }
}
