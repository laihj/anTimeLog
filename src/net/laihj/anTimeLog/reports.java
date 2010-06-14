package net.laihj.anTimeLog;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ArrayAdapter;
import android.view.View.OnClickListener;
import android.view.View;
import android.app.DatePickerDialog;

import java.util.Date;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import net.laihj.anTimeLog.DBHelper;

public class reports extends Activity
{
    /** Called when the activity is first created. */
    private DBHelper myDBHelper = null;
    private Button prev;
    private Button next;
    private Button start;
    private Button end;
    private Date startDate;
    private Date endDate;
    final static long ONE_DAY = 86400000;
    private ArrayList<reportItem> list = null;
    private reportAdapter adapter = null;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reports);

	myDBHelper = new DBHelper(this);

	ListView listView = (ListView) findViewById(R.id.reportlist);
	prev = (Button) findViewById(R.id.prev);
	next = (Button) findViewById(R.id.next);
	start = (Button) findViewById(R.id.reportfrom);
	end = (Button) findViewById(R.id.reportto);

	prev.setOnClickListener(listenser);
	next.setOnClickListener(listenser);
	start.setOnClickListener(listenser);
	end.setOnClickListener(listenser);

	startDate = getDateOnly(new Date());
	endDate = getDateOnly(new Date());
	end.setText(getDate(endDate));
	start.setText(getDate(startDate));

	list = (ArrayList<reportItem>) myDBHelper.getReport(startDate,endDate);
        adapter = new reportAdapter(this,list);
	listView.setAdapter(adapter);
    }

    private Date getDateOnly(Date datetime) {
	datetime.setHours(0);
	datetime.setMinutes(0);
	return datetime;
    }

    static private SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private String getDate(Date datetime) {
	return mDateFormat.format(datetime);
    }

   protected void onStop() {
	super.onStop();

	if(null != myDBHelper) {

	    myDBHelper.cleanup();
	    myDBHelper = null;
	}
    }

    DatePickerDialog.OnDateSetListener startDateListener = new DatePickerDialog.OnDateSetListener() {
	    public void onDateSet(DatePicker view,int year, int month ,int date) {
		startDate.setYear(year-1900);
		startDate.setMonth(month);
		startDate.setDate(date);
		updateReport();
	    }
	};
    
    DatePickerDialog.OnDateSetListener endDateListener = new DatePickerDialog.OnDateSetListener() {
	    public void onDateSet(DatePicker view,int year, int month ,int date) {
		endDate.setYear(year-1900);
		endDate.setMonth(month);
		endDate.setDate(date);
		updateReport();
	    }
	};

    private void updateReport() {
	end.setText(getDate(endDate));
	start.setText(getDate(startDate));
	list.clear();
	list.addAll(0,(ArrayList<reportItem>) myDBHelper.getReport(startDate,endDate));
	adapter.notifyDataSetChanged();
    }

    private OnClickListener listenser = new OnClickListener() {
	    public void onClick(View v) {
		switch(v.getId()) {
		case R.id.prev:
		    break;
		case R.id.next:
		    break;
		case R.id.reportfrom:
		    new DatePickerDialog(reports.this,startDateListener,startDate.getYear() + 1900,startDate.getMonth(),startDate.getDate()).show();

		    break;
		case R.id.reportto:
		    new DatePickerDialog(reports.this,endDateListener,endDate.getYear() + 1900,endDate.getMonth(),endDate.getDate()).show();

		    break;
		}
	    }
	};
}
