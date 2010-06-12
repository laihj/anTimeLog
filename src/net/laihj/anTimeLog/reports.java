package net.laihj.anTimeLog;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.GridView;
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

	GridView ReportGrid = (GridView) findViewById(R.id.grid);
	Date startDate = new Date();
	startDate.setHours(0);
	startDate.setMinutes(0);
	Date endDate = new Date(startDate.getTime() + 24*60*60*1000);
	final  ArrayList<String> list = (ArrayList<String>) myDBHelper.getReport(startDate, endDate);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list);
        ReportGrid.setNumColumns(3);
ReportGrid.setAdapter(adapter);
    }
}
