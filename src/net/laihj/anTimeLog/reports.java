package net.laihj.anTimeLog;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ArrayAdapter;

import java.util.Date;
import java.util.ArrayList;
import net.laihj.anTimeLog.DBHelper;

public class reports extends Activity
{
    /** Called when the activity is first created. */
    private DBHelper myDBHelper = null;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reports);

	myDBHelper = new DBHelper(this);

	ListView listView = (ListView) findViewById(R.id.reportlist);
	Date startDate = new Date();
	startDate.setHours(0);
	startDate.setMinutes(0);
	Date endDate = new Date(startDate.getTime() + 24*60*60*1000);
       	final  ArrayList<reportItem> list = (ArrayList<reportItem>) myDBHelper.getReport(startDate, endDate);
        reportAdapter adapter = new reportAdapter(this,list);
	listView.setAdapter(adapter);
    }
}
