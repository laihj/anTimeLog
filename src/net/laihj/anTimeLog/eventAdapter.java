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

public class eventAdapter extends BaseAdapter {

    private static final String CLASSTAG = eventAdapter.class.getSimpleName();
    private final Context context;
    private final List<eventItem> events;
    private DBHelper myDBHelper;

    public eventAdapter(Context context,List<eventItem> events) {
	this.context = context;
	this.events = events;
	myDBHelper = new DBHelper(context);
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
	return new eventListView(this.context, event);
    }

    private OnClickListener listenser = new OnClickListener() {
	    public void onClick(View v) {
		View container = (View)  v.getParent();
		eventItem event = ((eventListView) container).myEvent;
      		if(4 == v.getId()) {
		    //edit
		    Intent intent = new Intent("net.laihj.anTimeLog.action.EDIT_ITEM");
		    intent.putExtra("eventid",event.id);
		    ((Activity) v.getContext()).startActivityForResult(intent,1);
		    notifyDataSetChanged();
		    }else {
		    event.setEndTime(new Date());
		    myDBHelper.update(event);
		    notifyDataSetChanged();
      		}
	    }
	};

    private class eventListView extends RelativeLayout {
        public eventItem myEvent;
	private TextView event;
	private TextView theTime;
	private TextView type;
	private Button endBtn;
	private Button EditBtn;

	public eventListView(Context context, eventItem event) {
	    super(context);
            this.myEvent = event;
	    this.setFocusable(true);
	    this.setLongClickable(true);
	    RelativeLayout.LayoutParams rlEvent = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
										  ViewGroup.LayoutParams.WRAP_CONTENT);
	    this.event = new TextView(context);
	    this.event.setId(1);
	    this.event.setText(myEvent.event);
	    this.event.setTextSize(19f);
	    this.event.setTextColor(Color.WHITE);
	    this.addView(this.event,rlEvent);

	    RelativeLayout.LayoutParams rlType = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
										  ViewGroup.LayoutParams.WRAP_CONTENT);
	  
	    rlType.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
	    this.type = new TextView(context);
	    this.type.setPadding(5,5,5,5);
	    this.type.setId(2);
	    this.type.setText(event.type);
	    this.type.setTextSize(14f);
	    this.type.setTextColor(Color.WHITE);
	    this.addView(this.type,rlType);
            final RelativeLayout.LayoutParams rltheTime = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
										  ViewGroup.LayoutParams.WRAP_CONTENT);
	    rltheTime.addRule(RelativeLayout.BELOW,1);
	    this.theTime = new TextView(context);
	    this.theTime.setId(3);
	    this.theTime.setText(myEvent.getDuration());
	    this.theTime.setTextSize(14f);
	    this.theTime.setTextColor(Color.GRAY);
	    this.addView(this.theTime,rltheTime);
	    
	    final RelativeLayout.LayoutParams rleditBtn = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
										  ViewGroup.LayoutParams.WRAP_CONTENT);
	    rleditBtn.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
	    rleditBtn.addRule(RelativeLayout.BELOW,3);
	    rleditBtn.rightMargin = 5;
	    this.EditBtn = new Button(context);
	    this.EditBtn.setId(4);
	    this.EditBtn.setText("Edit");
	    this.EditBtn.setOnClickListener(listenser);
	    

	    final RelativeLayout.LayoutParams rlendBtn = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,

	      								   ViewGroup.LayoutParams.WRAP_CONTENT);
	    rlendBtn.addRule(RelativeLayout.BELOW,3);
	    rlendBtn.addRule(RelativeLayout.LEFT_OF,4);
	    rlendBtn.leftMargin = 10;
	    this.endBtn = new Button(context);
	    this.endBtn.setId(5);
	    this.endBtn.setText("END");
	    this.endBtn.setOnClickListener(listenser);
	    if(null == myEvent.getEndTime()){
		this.endBtn.setEnabled(true);
	    }else{
		this.endBtn.setEnabled(false);
	    }
	    setOnClickListener(new OnClickListener(){
		public void onClick(View v) {
		    if(-1 == indexOfChild(endBtn)) {
			addView(endBtn, rlendBtn);
			addView(EditBtn,rleditBtn);
		    }else{
                        removeView(endBtn);
			removeView(EditBtn);
		    }
		}
		});
	    /*	    setOnLongClickListener(new OnLongClickListener() {
		    public boolean onLongClick(View v) {
			Log.i("long","longClick2222");
			return true;
		    }
		    });*/
	}
    }
}