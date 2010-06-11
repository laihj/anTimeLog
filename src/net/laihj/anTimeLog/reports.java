package net.laihj.anTimeLog;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.GridView;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class reports extends Activity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reports);

	GridView ReportGrid = (GridView) findViewById(R.id.grid);
	final  ArrayList<String> list = new ArrayList<String> ();
	list.add("bbc");
	list.add("voa");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list);
        ReportGrid.setNumColumns(3);
ReportGrid.setAdapter(adapter);
    }
}
