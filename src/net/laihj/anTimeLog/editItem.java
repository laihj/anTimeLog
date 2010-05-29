package net.laihj.anTimeLog;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Button;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.app.TimePickerDialog;
import android.app.DatePickerDialog;
import android.view.View.OnClickListener;
import android.view.View;

import java.text.SimpleDateFormat;

import net.laihj.anTimeLog.eventItem;
import net.laihj.anTimeLog.DBHelper;

public class editItem extends Activity
{
    private eventItem theEvent;
    private DBHelper myDBHelper;
    private long eventId;

    private EditText eventText;
    private EditText typeText;
    private Button startDate ;
    private Button endDate;
    private Button startTime;
    private Button endTime;
    private Button ok;
    private Button cancel;

    private int mYear;
    private int mMonth;
    private int mDate;
    //final private int mHour;
    //final private int mMinute;
    //final private int mSecond;

    /** Called when the activity is first created. */    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.edit_item);

	myDBHelper = new DBHelper(this);

	eventText = (EditText) findViewById(R.id.eventText);
	typeText = (EditText) findViewById(R.id.typeText);
	startDate = (Button) findViewById(R.id.startDate);
	endDate = (Button) findViewById(R.id.endDate);
	startTime = (Button) findViewById(R.id.startTime);
	endTime = (Button) findViewById(R.id.endTime);
	ok = (Button) findViewById(R.id.OK);
	cancel = (Button) findViewById(R.id.cacel);

	Bundle extras = getIntent().getExtras();
        eventId = extras.getLong("eventid");
	theEvent = myDBHelper.getItemById(eventId);
	initComponent(theEvent);

        startDate.setOnClickListener(listenser);
	endDate.setOnClickListener(listenser);
	startTime.setOnClickListener(listenser);
	endTime.setOnClickListener(listenser);
    }

    private OnClickListener listenser = new OnClickListener() {
	    public void onClick(View v) {
	
		new DatePickerDialog(editItem.this,dateListener,theEvent.getStartTime().getYear() + 1900,theEvent.getStartTime().getMonth(),theEvent.getStartTime().getDate()).show();
	
	    }
	};

    DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
	    public void onDateSet(DatePicker view,int year, int month ,int date) {
		mYear = year;
		mMonth = month;
		mDate = date;
		theEvent.getStartTime().setYear(mYear-1900);
		theEvent.getStartTime().setMonth(mMonth);
		theEvent.getStartTime().setDate(mDate);
	        startDate.setText(theEvent.getStartDate());
	    }
	};

    private void initComponent(eventItem event) {
	eventText.setText(event.event);
	typeText.setText(event.type);
	startDate.setText(event.getStartDate());
	startTime.setText(event.getStartTimes());
	endDate.setText(event.getEndDate());
	endTime.setText(event.getEndTimes());
	
    }
}
