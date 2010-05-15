package net.laihj.anTimeLog;

import java.util.Date;

public class eventItem
    {
	public long id;
	public String event;
	public Date startTime;
	public Date endTime;
	public String type;
	public String duration;

	public eventItem() { }

	public eventItem(final String event, final Date startTime) {
	    this.event = event;
	    this.startTime = startTime;
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