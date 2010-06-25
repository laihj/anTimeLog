package net.laihj.anTimeLog;

import android.util.Log;

import android.app.Application;

import net.laihj.anTimeLog.DBHelper;

public class anTimeLogApplication extends Application {

    private DBHelper theDBHelper;

    public anTimeLogApplication() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public DBHelper getDatabase() {
	Log.i("forFC","getData");
	if (null == this.theDBHelper) {
	    Log.i("forFC","new");
	    this.theDBHelper = new DBHelper(this);
	}
	return this.theDBHelper;
    }
}
