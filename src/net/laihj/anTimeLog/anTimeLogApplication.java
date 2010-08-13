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
	Log.i("backup","getDatabase");
	if (null == this.theDBHelper) {
	Log.i("backup","get new");	    
	    this.theDBHelper = new DBHelper(this);
	}
	return this.theDBHelper;
    }

    public void shutdownDataBase() {
	if (null != this.theDBHelper ) {
	this.theDBHelper.cleanup();
	this.theDBHelper = null;
	}
    }
}
