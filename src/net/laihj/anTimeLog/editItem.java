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
import java.util.Date;

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




    /** Called when the activity is first created. */    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.edit_item);

	anTimeLogApplication application = (anTimeLogApplication) getApplication();
        myDBHelper = application.getDatabase();


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

	ok.setOnClickListener( new OnClickListener() {
		public void onClick(View v) {
		    theEvent.event = eventText.getText().toString();
		    theEvent.type = typeText.getText().toString();
		    myDBHelper.update(theEvent);
		    editItem.this.setResult(10,editItem.this.getIntent());
		    editItem.this.finish();
		}
	    });
	cancel.setOnClickListener(new OnClickListener() {
		public void onClick(View v) {
		    editItem.this.finish();
		}
	    });
    }

    private OnClickListener listenser = new OnClickListener() {
	    public void onClick(View v) {
		Date pickerDate ; 
		if( R.id.startDate == v.getId() ) {
		new DatePickerDialog(editItem.this,startDateListener,theEvent.getStartTime().getYear() + 1900,theEvent.getStartTime().getMonth(),theEvent.getStartTime().getDate()).show();
		} else if( R.id.startTime == v.getId()) {
		    new TimePickerDialog(editItem.this, startTimeListener, theEvent.getStartTime().getHours(), theEvent.getStartTime().getMinutes(), true).show();
		} else if( R.id.endDate == v.getId() ) {
	
		    if (null == theEvent.getEndTime()) {
			pickerDate = new Date();
		    } else {
			pickerDate = theEvent.getEndTime();
		    }
		    new DatePickerDialog(editItem.this, endDateListener, pickerDate.getYear() + 1900, pickerDate.getMonth(), pickerDate.getDate()).show();
		} else if( R.id.endTime == v.getId() ) {
		    if (null == theEvent.getEndTime()) {
			pickerDate = new Date();
		    } else {
			pickerDate = theEvent.getEndTime();
		    }
		    new TimePickerDialog(editItem.this, endTimeListener, pickerDate.getHours(), pickerDate.getMinutes(), true).show();
		}
	    }
	};

    TimePickerDialog.OnTimeSetListener endTimeListener = new TimePickerDialog.OnTimeSetListener() {
	    public void onTimeSet(TimePicker view, int hour, int minute) {
		if (null == theEvent.getEndTime()) {
		    theEvent.setEndTime(new Date());
		}
		theEvent.getEndTime().setHours(hour);
		theEvent.getEndTime().setMinutes(minute);
		endTime.setText(theEvent.getEndTimes());
	    }
	};


    DatePickerDialog.OnDateSetListener endDateListener = new DatePickerDialog.OnDateSetListener() {
	    public void onDateSet(DatePicker view,int year, int month ,int date) {
		if(null == theEvent.getEndTime()) {
		    theEvent.setEndTime(new Date());
		}
		theEvent.getEndTime().setYear(year-1900);
		theEvent.getEndTime().setMonth(month);
		theEvent.getEndTime().setDate(date);
	        endDate.setText(theEvent.getEndDate());
	    }
	};

    TimePickerDialog.OnTimeSetListener startTimeListener = new TimePickerDialog.OnTimeSetListener() {
	    public void onTimeSet(TimePicker view, int hour, int minute) {
		theEvent.getStartTime().setHours(hour);
		theEvent.getStartTime().setMinutes(minute);
		startTime.setText(theEvent.getStartTimes());
	    }
	};

    DatePickerDialog.OnDateSetListener startDateListener = new DatePickerDialog.OnDateSetListener() {
	    public void onDateSet(DatePicker view,int year, int month ,int date) {
		theEvent.getStartTime().setYear(year-1900);
		theEvent.getStartTime().setMonth(month);
		theEvent.getStartTime().setDate(date);
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
