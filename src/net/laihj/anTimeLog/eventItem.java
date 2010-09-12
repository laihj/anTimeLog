package net.laihj.anTimeLog;

import java.util.Date;
import java.lang.StringBuilder;
import java.text.SimpleDateFormat;

public class eventItem
    {
	private boolean timeChanged=true;
	public long id;
	public String event;
	private Date startTime;
	public String note;
	static private SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	static private SimpleDateFormat mTimeFormat = new SimpleDateFormat("HH:mm");
	public Date getStartTime() {
	    return this.startTime;
	}
	public void setStartTime(Date startTime) {
	    this.startTime = startTime;
	    this.timeChanged = true;
	}

	public String getStartDate() {
	    if (null == this.startTime) {
		return "StartDate";
	    } else {
		return mDateFormat.format(this.startTime);
	    }
	}
	
	public String getStartTimes() {
	    if (null == this.startTime) {
		return "StartTime";
	    } else {
		return mTimeFormat.format(this.startTime);
	    }
	}
	
	private Date endTime;
	public Date getEndTime() {
	    return this.endTime;
	}
	public void setEndTime(Date endTime) {
	    this.endTime = endTime;
	    this.timeChanged = true;
	}

	public String getEndDate() {
	    if (null == this.endTime) {
		return "EndDate";
	    } else {
		return mDateFormat.format(this.endTime);
	    }
	}

	public String getEndTimes() {
	    if(null == this.endTime) {
		return "EndTime";
	    } else {
		return mTimeFormat.format(this.endTime);
	    }
	}
	
	public String type;
	private String duration;
	public String getDuration() {
	    if (true) {
	        StringBuilder sb = new StringBuilder();
	        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	        sb.append(dateFormat.format(this.startTime));
	    
	        if(null == endTime) {
		    
	        } else {
		    sb.append(diff(endTime,startTime));
	        }
	        this.timeChanged = false;
	        this.duration =  sb.toString();
	    }
	    return this.duration;
	}

	private String diff(Date endTime, Date startTime) {
	    long seconds;
	    seconds = (endTime.getTime() - startTime.getTime()) / 1000;
	    int hours = (int) seconds / ( 60 * 60 );
	    seconds = seconds - hours * 3600 ;
	    int minute = (int) seconds / 60 ;
	    StringBuilder dura = new StringBuilder();
	    if(hours > 0 ) {
		dura.append( " " + hours + "hr ");
	    }
	    if(minute > 0 ) {
		dura.append( " " + minute + " min" );
	    }
	    if (dura.length() == 0) {
		return " less than one minute";
	    } else {
	    return dura.toString();
	    }
	    
	}

	public eventItem() { }

	public eventItem(final String event, final Date startTime) {
	    this.event = event;
	    this.startTime = startTime;
	}

	public eventItem(final long id, final String event, final Date startTime, final Date endTime, final String type, final String duration,final String note)
	{
	    this.id = id;
	    this.event = event;
	    this.startTime = startTime;
	    this.endTime = endTime;
	    this.type = type;
	    this.duration = duration;
	    this.note = note;
	}
	
    }