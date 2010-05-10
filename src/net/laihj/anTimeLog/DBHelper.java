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

    public static class eventItem
    {
	public long id;
	public String event;
	public Date startTime;
	public Date endTime;
	public String type;
	public String duration;

	public eventItem() { }

	public eventItem(final long id, final String event, final Date startTime, final Date endTime, final String type, final String duration)
	{
	    this.id = id;
	    this.event = event;
	    this.startTime = startTime;
	    this.endTime = endTime;
	    this.type = type;
	    this.duration = duration;
	}
	
    }

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

    public void insert(final eventItem event) {
    }

    public void update(final eventItem event) {
    }

    public void delete(final long id) {
    }

       


    
}