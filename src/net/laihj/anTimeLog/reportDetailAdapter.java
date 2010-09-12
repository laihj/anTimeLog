package net.laihj.anTimeLog;

import android.view.View.OnLongClickListener;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Button;
import android.app.Activity;
import android.content.Intent;


import net.laihj.anTimeLog.eventItem;

import java.util.List;
import java.util.Date;

public class reportDetailAdapter extends BaseAdapter {

    private static final String CLASSTAG = eventAdapter.class.getSimpleName();
    private final Context context;
    private final List<eventItem> events;

    public reportDetailAdapter(Context context,List<eventItem> events) {
	this.context = context;
	this.events = events;
    }

    public int getCount() {
	return this.events.size();
    }

    public Object getItem(int Position) {
	return this.events.get(Position);
    }

    public long getItemId(int Position) {
	return Position;
    }

    public View getView(int Position, View convertView, ViewGroup parent) {
	eventItem event = this.events.get(Position);
	return new reportDetailListView(this.context, event);
    }

    private class reportDetailListView extends RelativeLayout {
        public eventItem myEvent;
	private TextView theTime;
	private TextView event;
	private Context context;
	private TextView note;

	public reportDetailListView(Context context, eventItem event) {
	    super(context);
            this.myEvent = event;
	    this.context = context;


            final RelativeLayout.LayoutParams rltheTime = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
										  ViewGroup.LayoutParams.WRAP_CONTENT);
	    this.theTime = new TextView(context);
	    this.theTime.setId(3);
	    this.theTime.setText(myEvent.getDuration());
	    this.theTime.setTextSize(16f);
	    this.theTime.setPadding(1,1,0,1);
	    this.theTime.setTextColor(Color.WHITE);
	    this.addView(this.theTime,rltheTime);
            final RelativeLayout.LayoutParams rltheNote = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
										  ViewGroup.LayoutParams.WRAP_CONTENT);
	    rltheNote.addRule(RelativeLayout.BELOW,3);
	    this.note = new TextView(context);
	    this.note.setId(4);
	    this.note.setText(myEvent.note);
	    this.note.setTextSize(16f);
	    this.note.setPadding(12,1,0,1);
	    this.note.setTextColor(Color.WHITE);


      	    if(0 != this.note.getText().toString().trim().length()) {

		    this.addView(this.note,rltheNote);
	    }
	}
    }
}