package net.laihj.anTimeLog;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;



public class DBHelper
{
    public static final String LOG_TAG = "DBHelper";
    public static final String DB_NAME = "time_log";
    public static final String DB_TABLE = "your_time_log";
    public static final int DB_VERSION = 3;

    private static final String CLASSNAME = DBHelper.class.getSimpleName();
    private static final String[] COLS = new String[] { "_id", "event", "startTime", "endTime", "type"};

    private SQLiteDatabase db;
    private final DBOpenHelper dbOpenHelper;

    private static class DBOpenHelper extends SQLiteOpenHelper {

        private static final String DB_CREATE = "CREATE TABLE "
            + DBHelper.DB_TABLE
            + " (_id INTEGER PRIMARY KEY, event TEXT, startTime TEXT, endTime  TEXT, type TEXT);";

        public DBOpenHelper(final Context context) {
            super(context, DBHelper.DB_NAME, null, DBHelper.DB_VERSION);
        }

        @Override
        public void onCreate(final SQLiteDatabase db) {
            try {
                db.execSQL(DBOpenHelper.DB_CREATE);
            } catch (SQLException e) {
                Log.e(LOG_TAG, DBHelper.CLASSNAME, e);
            }
        }

        @Override
        public void onOpen(final SQLiteDatabase db) {
            super.onOpen(db);
        }

        @Override
        public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + DBHelper.DB_TABLE);
            onCreate(db);
        }
    }

    public DBHelper(final Context context)
    {
	this.dbOpenHelper = new DBOpenHelper(context);
	establishDb();
    }

    private void establishDb() {
        if (this.db == null) {
            this.db = this.dbOpenHelper.getWritableDatabase();
        }
    }

    public void cleanup() {
        if (this.db != null) {
            this.db.close();
            this.db = null;
        }
    }

    public long insert(final eventItem event) {
	ContentValues values = new ContentValues();
	values.put("event",event.event);
	values.put("startTime",event.startTime.toString());
	if(null == event.endTime) {
	    values.put("endTime","");
	} else {
	    values.put("endTime",event.endTime.toString());	    
	}

	values.put("type",event.type);
	return this.db.insert(DBHelper.DB_TABLE, null, values);
    }

    public void update(final eventItem event) {
	ContentValues values = new ContentValues();
	values.put("event",event.event);
	values.put("startTime",event.startTime.toString());
	values.put("endTime",event.endTime.toString());
	values.put("type",event.type);
	this.db.update(DBHelper.DB_TABLE,values,"_id=" + event.id, null);
    }

    public void delete(final long id) {
	this.db.delete(DBHelper.DB_TABLE,"_id=" + id,null);
    }

    public List<eventItem> getAll() {
	ArrayList<eventItem> ret = new ArrayList<eventItem> ();
	Cursor c = null;
	try {
	    c = this.db.query(DBHelper.DB_TABLE, DBHelper.COLS, null, null, null, null, null);
	    int numRows = c.getCount();
	    c.moveToFirst();

	    for( int i = 0 ;i < numRows ; i ++) {
		eventItem event = new eventItem();
		event.id = c.getLong(0);
		event.event = c.getString(1);
		event.startTime = new Date(c.getString(2));
		event.endTime = new Date(c.getString(3));
		event.type = c.getString(4);
		//event.duration
		ret.add(event);
		c.moveToNext();
	    }
	} catch (SQLException e) {
	    Log.v(LOG_TAG,DBHelper.CLASSNAME, e);
	} finally {
	    if (c != null && !c.isClosed()) {
		c.close();
	    }
	}
	return ret;
    }
}