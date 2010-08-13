package net.laihj.anTimeLog;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.lang.Exception;

public class BackupHelper {
    public static final String DBName = "time_log";
    public static final String PackageName = "net.laihj.anTimeLog";

    public static boolean BackupDatabase () {
	Log.i("backup","begin");
	try {
	    File sd = Environment.getExternalStorageDirectory();
	    File data = Environment.getDataDirectory();
	    if (sd.canWrite()) {
	Log.i("backup","begin");
		String currentDBPath = "\\data\\" + PackageName + "\\databases\\" + DBName + "";
		String backupDBPath = DBName;
		File currentDB = new File(data, currentDBPath);
	Log.i("backup","currentDBPath");
		File backupDB = new File(sd, backupDBPath);
		if (currentDB.exists()) {
	Log.i("backup","exsits");
		    FileChannel src = new FileInputStream(currentDB).getChannel();
		    FileChannel dst = new FileOutputStream(backupDB).getChannel();
		    dst.transferFrom(src, 0, src.size());
		    src.close();
		    dst.close();
		}
	    }
	} catch (Exception e) {
	    Log.i("backup",e.toString());
	    return false;
	}
	return true;
    }
}