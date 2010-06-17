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
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;


import java.util.Calendar;
import java.util.Date;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import net.laihj.anTimeLog.DBHelper;

public class reportDetail extends Activity
{
    /** Called when the activity is first created. */
    private DBHelper myDBHelper = null;
    private String event;
    private Button prev;
    private Button next;
    private Button start;
    private Button end;
    private Date startDate;
    private Date endDate;
    final static long ONE_DAY = 86400000;
    private ArrayList<eventItem> list = null;
    private reportDetailAdapter adapter = null;
    private long diffDate;
    private ListView listView;

    private boolean isMonth;

    public Date getStartDate() {
	return startDate;
    }

    public Date getEndDate() {
	return endDate;
    }

    //position of OptionsMenu
    final static private int MENU_TODAY = Menu.FIRST;
    final static private int MENU_TWEEK = Menu.FIRST + 1;
    final static private int MENU_TMON = Menu.FIRST + 2;
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reports);

	myDBHelper = new DBHelper(this);

	listView = (ListView) findViewById(R.id.reportlist);
	prev = (Button) findViewById(R.id.prev);
	next = (Button) findViewById(R.id.next);
	start = (Button) findViewById(R.id.reportfrom);
	end = (Button) findViewById(R.id.reportto);

	prev.setOnClickListener(listenser);
	next.setOnClickListener(listenser);
	start.setOnClickListener(listenser);
	end.setOnClickListener(listenser);


	Bundle extras = getIntent().getExtras();

	event = extras.getString("event");
        startDate = new Date(extras.getLong("startDate"));
        endDate = new Date(extras.getLong("endDate"));
	diffDate = endDate.getTime() - startDate.getTime() + ONE_DAY;
	TextView header = new TextView(this);
	header.setText(event);
	header.setTextSize(19f);
	header.setGravity(Gravity.CENTER);

	listView.addHeaderView(header);
        
	updateReport();

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
		diffDate = endDate.getTime() - startDate.getTime() + ONE_DAY;
		isMonth = false;
		updateReport();
	    }
	};
    
    DatePickerDialog.OnDateSetListener endDateListener = new DatePickerDialog.OnDateSetListener() {
	    public void onDateSet(DatePicker view,int year, int month ,int date) {
		endDate.setYear(year-1900);
		endDate.setMonth(month);
		endDate.setDate(date);
		diffDate = endDate.getTime() - startDate.getTime() + ONE_DAY;
		isMonth = false;
		updateReport();
	    }
	};

    private void updateReport() {
	end.setText(getDate(endDate));
	start.setText(getDate(startDate));
	list = (ArrayList<eventItem>) myDBHelper.getReportEvent(event,startDate,endDate);
        adapter = new reportDetailAdapter(this,list);
	listView.setAdapter(adapter);
    }

    private OnClickListener listenser = new OnClickListener() {
	    public void onClick(View v) {
		switch(v.getId()) {
		case R.id.prev:
		    if(isMonth) {
			Calendar rightNow = Calendar.getInstance();
			rightNow.setTime(startDate);
			rightNow.add(Calendar.MONTH,-1);
			setMonth(rightNow);
		    } else {
			startDate = new Date( startDate.getTime() - diffDate);
			endDate = new Date( endDate.getTime() - diffDate);
		    }
		    updateReport();
		    break;
		case R.id.next:
		    if(isMonth) {
			Calendar rightNowNext = Calendar.getInstance();
			rightNowNext.setTime(startDate);
			rightNowNext.add(Calendar.MONTH,1);
			setMonth(rightNowNext);
		    } else {
			startDate = new Date( startDate.getTime() + diffDate);
			endDate = new Date( endDate.getTime() + diffDate);
		    }
		    updateReport();
		    break;
		case R.id.reportfrom:
		    new DatePickerDialog(reportDetail.this,startDateListener,startDate.getYear() + 1900,startDate.getMonth(),startDate.getDate()).show();

		    break;
		case R.id.reportto:
		    new DatePickerDialog(reportDetail.this,endDateListener,endDate.getYear() + 1900,endDate.getMonth(),endDate.getDate()).show();

		    break;
		}
	    }
	};

        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, MENU_TODAY, 0, R.string.today).setIcon(
            android.R.drawable.ic_menu_day);
	menu.add(0, MENU_TWEEK, 0, R.string.thisweek).setIcon(android.R.drawable.ic_menu_week);
	menu.add(0, MENU_TMON, 0, R.string.thismonth).setIcon(android.R.drawable.ic_menu_month);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case MENU_TODAY:
	    setToday();
	    return true;
	case MENU_TWEEK:
	    setThisWeek();
            return true;
	case MENU_TMON:
	    Calendar rightNow = Calendar.getInstance();
	    setMonth(rightNow);
	    return true;
        }
        return true;
    }

    private void setToday() {
	startDate = getDateOnly(new Date());
	endDate = getDateOnly(new Date());
        diffDate = ONE_DAY;
	isMonth = false;
	updateReport();
    }

    private void setThisWeek() {
	startDate = new Date(new Date().getTime() - new Date().getDay() * ONE_DAY);
	endDate = new Date(startDate.getTime() + 6 * ONE_DAY);
	diffDate = 7 * ONE_DAY;
	isMonth = false;
	updateReport();
    }

    private void setMonth(Calendar rightNow) {
	rightNow.set(Calendar.DATE, rightNow.getActualMinimum(Calendar.DATE));
	startDate = getDateOnly(rightNow.getTime());
	rightNow.set(Calendar.DATE, rightNow.getActualMaximum(Calendar.DATE));
	endDate = getDateOnly(rightNow.getTime());
	isMonth = true;
	updateReport();
    }

}
