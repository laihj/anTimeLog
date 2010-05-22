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
	public Date getStartTime() {
	    return this.startTime;
	}
	public void setStartTime(Date startTime) {
	    this.startTime = startTime;
	    this.timeChanged = true;
	}
	
	private Date endTime;
	public Date getEndTime() {
	    return this.endTime;
	}
	public void setEndTime(Date endTime) {
	    this.endTime = endTime;
	    this.timeChanged = true;
	}
	public String type;
	private String duration;
	public String getDuration() {
	    if (true) {
	        StringBuilder sb = new StringBuilder();
	        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        sb.append(dateFormat.format(this.startTime));
	    
	        if(null == endTime) {
	        } else {
		
	        }
	        this.timeChanged = false;
	        this.duration =  sb.toString();
	    }
	    return this.duration;
	}
	

	public eventItem() { }

	public eventItem(final String event, final Date startTime) {
	    this.event = event;
	    this.startTime = startTime;
	    this.endTime = startTime;
	    this.type = "default";
	}

	public eventItem(final long id, final String event, final Date startTime, final Date endTime, final String type, final String duration)
	{
	    this.id = id;
	    this.event = event;
	    this.startTime = startTime;
	    this.endTime = endTime;
	    this.type = type;
	    this.duration = duration;
	}
	
    }