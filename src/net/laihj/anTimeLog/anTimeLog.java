package net.laihj.anTimeLog;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Button;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View;
import android.util.Log;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AutoCompleteTextView;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.app.Dialog;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;
import android.content.res.Resources;
import android.os.Handler;
import android.view.KeyEvent;
import android.app.ProgressDialog;
import android.os.Message;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.util.Linkify;
import android.text.method.LinkMovementMethod;
import android.text.SpannableString;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.widget.RemoteViews;
import android.view.inputmethod.InputMethodManager;
import android.content.Context;


import net.laihj.anTimeLog.eventItem;
import net.laihj.anTimeLog.eventAdapter;
import net.laihj.anTimeLog.DBHelper;

import java.util.ArrayList;
import java.util.Date;
import java.lang.CharSequence;
import java.lang.Integer;

public class anTimeLog extends Activity
{
    final static private int LONGCLICK = 1;
    final static private int SETTINGS = 2;
    final static private int ABOUT = 3;
    final static private int DO_IT = 1;
    final static private int CLICKITEM = 1984;

    //position of OptionsMenu
    final static private int MENU_SETTING = Menu.FIRST;
    final static private int MENU_ALA = Menu.FIRST + 1;
    final static private int MENU_ABOUT = Menu.FIRST + 2;
    final static private int CLEARALL = Menu.FIRST + 3;
    
    //position of contextMenu
    final static private int CON_END = 0;
    final static private int CON_EDIT = CON_END + 1;
    final static private int CON_DO_IT = CON_EDIT + 1;
    final static private int CON_DELETE = CON_DO_IT + 1;
    final static private int CON_REPROT_THIS = CON_DELETE + 1;
    final static private int CON_REPROT_TYPE = CON_REPROT_THIS + 1;
    

    
    private ArrayList<eventItem> events;
    private eventAdapter myAdapter;
    private DBHelper myDBHelper;
    private AutoCompleteTextView quickText ;
    public ListView list;
    public eventItem selectedEvent = null;
    private ProgressDialog progressDialog;
    private Resources res;
    private int display_num = 30;
    /** Called when the activity is first created. */

    private NotificationManager mNotificationManager;
    private boolean showIcon;
    private boolean needReflash;
    private final Handler handler = new Handler () {
	    @Override
	    public void handleMessage ( Message msg ) {
		    myAdapter = new eventAdapter(anTimeLog.this, events);
		    list.setAdapter(myAdapter);
		    list.setSelection(events.size()-1);
		    eventNotify();
		    needReflash = false;
	    }
	};
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
	//	if(keyCode == KeyEvent.KEYCODE_BACK) {
	//  dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN,KeyEvent.KEYCODE_HOME));
	//  return true;
	//	}
	return super.onKeyDown(keyCode,event);
    }
    private void eventNotify() {
	if (!showIcon) {
	    return;
	}
	Notification notif = new Notification();
	PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
								new Intent(this, anTimeLog.class)
								.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
								PendingIntent.FLAG_UPDATE_CURRENT);
	notif.contentIntent = contentIntent;
	
	notif.icon = R.drawable.antimelog;
	notif.flags = Notification.FLAG_ONGOING_EVENT;
	
	RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.notify);

	String eventtext = res.getString(R.string.tracking);
	String time = "";

	if(null == this.events || this.events.size() == 0) {
	} else {
	    if ( null == events.get(events.size() -1).getEndTime() ) {
		eventtext = events.get(events.size() -1).event;
		time = events.get(events.size() - 1 ) .getDuration();
	    }
	}
	
        contentView.setTextViewText(R.id.eventtext, eventtext);
	notif.tickerText = eventtext;
	contentView.setTextViewText(R.id.time, time);
	
        contentView.setImageViewResource(R.id.icon, R.drawable.antimelog);
        notif.contentView = contentView;
        mNotificationManager.notify(R.layout.notify, notif);

    }

    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.antimelog);
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
	//	myDBHelper = new DBHelper(this);

	list = (ListView) findViewById(R.id.list);
	quickText = (AutoCompleteTextView) findViewById(R.id.quickAddText);
	Button quickButton = (Button) findViewById(R.id.quickAddButton);
        res = getResources();
	events = new ArrayList<eventItem> ();
	//get all list first
	needReflash = true;


	quickButton.setOnClickListener(new OnClickListener() {
		public void onClick(View v) {
		    eventItem ei = new eventItem(quickText.getText().toString(), new Date());
		    anTimeLog.this.addEvent(ei);

		}
	    });
    }
    

    protected void onDestroy() {
	super.onDestroy();
	//mNotificationManager.cancel(R.layout.notify);
    }


    private void addEvent(eventItem ei) {
	  ei.id = myDBHelper.insert(ei);
	
	  if( events.size() > 0 ) {
	       if(null == events.get(events.size() - 1).getEndTime()) {
		     events.get(events.size() - 1).setEndTime(new Date());
		     myDBHelper.update(events.get(events.size() - 1));
		     }
		}
	    events.add(ei);
	
	    if(events.size() > display_num) {
	
		events.remove(0);
	    }
	    myAdapter.notifyDataSetChanged();
	    quickText.setText("");
	    eventNotify();
    }


    public boolean onContextItemSelected(MenuItem item) {
	AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
	eventItem ei = new eventItem(events.get((int) info.id).event, new Date());
	ei.type = events.get((int)info.id).type;
	addEvent(ei);
	return true;
    }


    @Override
    protected void onPrepareDialog(int id, final Dialog dialog) {
	switch( id) {
	case CLICKITEM:
	    if (null == selectedEvent ) {
		break;
	    } else {
		((AlertDialog) dialog).setTitle(selectedEvent.event);
		break;
	    }
	}
    }
     @Override
    protected Dialog onCreateDialog(int id) {
	 switch( id ) {
	 case CLICKITEM :
	     if ( null == selectedEvent ){
		 return null;
	     } else {
		 CharSequence [] conMenu;
		 conMenu = res.getTextArray(R.array.conmenu);
		 return new AlertDialog.Builder(anTimeLog.this)
		     .setTitle(selectedEvent.event)
		     .setNegativeButton(res.getText(R.string.donothing), new DialogInterface.OnClickListener() {
			     public void onClick(DialogInterface dialog, int which) {
			     }
			 })
		     .setItems(conMenu, new DialogInterface.OnClickListener() {
			     public void onClick(DialogInterface dialog, int which) {
				 switch( which ) {
				 case CON_END:
				     if(null == selectedEvent.getEndTime()) {
					 selectedEvent.setEndTime(new Date());
					 anTimeLog.this.myDBHelper.update(selectedEvent);
					 anTimeLog.this.myAdapter.notifyDataSetChanged();
				     }
				     eventNotify();
				     break;
				 case CON_EDIT:
				     Intent intent = new Intent("net.laihj.anTimeLog.action.EDIT_ITEM");
				     intent.putExtra("eventid",selectedEvent.id);
				     anTimeLog.this.startActivityForResult(intent,1);
				     break;
				 case CON_DO_IT:
				     eventItem ei = new eventItem(selectedEvent.event, new Date());
				     ei.type = selectedEvent.type;
				     anTimeLog.this.addEvent(ei);
				     eventNotify();
				     break;
				 case CON_DELETE:
				     myDBHelper.delete(selectedEvent.id);
				     anTimeLog.this.events.remove(selectedEvent);
				     anTimeLog.this.myAdapter.notifyDataSetChanged();
				     selectedEvent = null;
				     eventNotify();
				     break;
				 case CON_REPROT_THIS:
				     break;
				 case CON_REPROT_TYPE:
				     break;
				 }
				     
			     }
			 })
		     .create();
			 }
	 case ABOUT:
	     final TextView message = new TextView(this);
	     final SpannableString s = 
             new SpannableString(res.getText(R.string.author_info));
	     Linkify.addLinks(s, Linkify.ALL);
	     message.setText(s);
	     message.setMovementMethod(LinkMovementMethod.getInstance());
	     message.setTextSize(19f);

	     return new AlertDialog.Builder(anTimeLog.this)
                .setTitle(R.string.about)
		 .setView(message)
                .create();

	 }
	 return null;
     }
    @Override
    public void onResume() {
	super.onResume();
	SharedPreferences shardPre = PreferenceManager.getDefaultSharedPreferences(this);
	if( display_num != Integer.parseInt(shardPre.getString("dispaly_preference","30"))) {
	    display_num = Integer.parseInt(shardPre.getString("dispaly_preference","30"));
	    needReflash = true;

	}
	showIcon = shardPre.getBoolean("status_icon_preference",true);
	if(!showIcon) {
	    mNotificationManager.cancel(R.layout.notify);
	}
	anTimeLogApplication application = (anTimeLogApplication) getApplication();
        myDBHelper = application.getDatabase();
	//       	this.progressDialog = ProgressDialog.show(this, " Loading...", " Londing events", true, false);
	if(needReflash) { 
	    new Thread() {
		@Override
		    public void run() {
		    events = (ArrayList<eventItem>) myDBHelper.getAll(display_num);
		    handler.sendEmptyMessage(0);
		}
	    }.start();
	}
	InputMethodManager imm = (InputMethodManager) getSystemService (Context.INPUT_METHOD_SERVICE); 

	imm.hideSoftInputFromWindow(quickText.getWindowToken(), 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, anTimeLog.MENU_SETTING, 0, R.string.setting).setIcon(
            android.R.drawable.ic_menu_preferences);
	menu.add(0, anTimeLog.MENU_ALA, 0, R.string.analysis).setIcon(android.R.drawable.ic_menu_report_image);
	menu.add(0, anTimeLog.MENU_ABOUT, 0, R.string.about).setIcon(android.R.drawable.ic_menu_info_details);
	menu.add(0,anTimeLog.CLEARALL, 0 , R.string.about);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case anTimeLog.MENU_SETTING:
            Intent intent = new Intent("net.laihj.anTimeLog.action.SETTING");
	    startActivityForResult(intent,SETTINGS);
	    return true;
	case anTimeLog.MENU_ALA:
	    Intent intentReport = new Intent("net.laihj.anTimeLog.action.REPORT");
	    startActivity(intentReport);
            return true;
	case anTimeLog.MENU_ABOUT:
	    showDialog(ABOUT);
	    return true;
	case anTimeLog.CLEARALL:
	    Log.i("clear","aa");
	    myDBHelper.clearall();
	    this.events.clear();
	    this.eventAdapter.notifyDataSetChanged();
	    return true;
        }
        return true;
    }


    
    @Override
	protected void onActivityResult(int requestCode, int resultCode,
					Intent data) {
	super.onActivityResult(requestCode,resultCode,data);
	switch ( resultCode ) {
	case 10:
	    Long editedEvent;
	    editedEvent = data.getExtras().getLong("eventid");
	    for(int i = 0 ; i < events.size() ;i ++) {
		if(events.get(i).id == editedEvent) {
		    myDBHelper.freshItem(events.get(i));
		    myAdapter.notifyDataSetChanged();
		    break;
		}
	    }
	    eventNotify();
	    break;
	}
    }
}
