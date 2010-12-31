package net.laihj.anTimeLog;

import android.widget.Toast;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Button;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.view.View;
import android.util.Log;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AutoCompleteTextView;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.app.Dialog;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.content.res.Resources;
import android.os.Handler;
import android.view.KeyEvent;
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
import android.widget.LinearLayout;
import android.view.ViewGroup;
import android.webkit.WebView;


import net.laihj.anTimeLog.eventItem;
import net.laihj.anTimeLog.eventAdapter;
import net.laihj.anTimeLog.DBHelper;

import java.util.ArrayList;
import java.util.Date;
import java.lang.CharSequence;
import java.lang.Integer;

public class anTimeLog extends Activity
{
    final static private int SETTINGS = 2;
    final static private int ABOUT = 3;
    final static private int CLICKITEM = 1984;
    final static private int CLEARCONFIRM = 1989;
    final static private int FILESELECT = 1990;
    final static private int RECORVERYCONFIRM = 1991;
    //position of OptionsMenu
    final static private int MENU_SETTING = Menu.FIRST;
    final static private int MENU_ALA = Menu.FIRST + 1;
    final static private int MENU_ABOUT = Menu.FIRST + 2;
    final static private int CLEARALL = Menu.FIRST + 3;
    final static private int MENU_BACKUP = Menu.FIRST + 4;
    final static private int MENU_RECOVERY = Menu.FIRST + 5;
    
    //position of contextMenu
    final static private int CON_END = 0;
    final static private int CON_EDIT = CON_END + 1;
    final static private int CON_DO_IT = CON_EDIT + 1;
    final static private int CON_DELETE = CON_DO_IT + 1;
    final static private int CON_REPROT_THIS = CON_DELETE + 1;
    final static private int CON_REPROT_TYPE = CON_REPROT_THIS + 1;
    

    public CharSequence [] backupFile;    
    private ArrayList<eventItem> events;
    private eventAdapter myAdapter;
    private DBHelper myDBHelper;
    private AutoCompleteTextView quickText ;
    public ListView list;
    public eventItem selectedEvent = null;
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
	list = (ListView) findViewById(R.id.list);
	quickText = (AutoCompleteTextView) findViewById(R.id.quickAddText);
	Button quickButton = (Button) findViewById(R.id.quickAddButton);
        res = getResources();
	events = new ArrayList<eventItem> ();
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
    public String seletedFile;
     @Override
    protected Dialog onCreateDialog(int id) {
	 switch( id ) {
	 case CLEARCONFIRM:
	     return getClearConfirmDialog();
	 case RECORVERYCONFIRM:
	     return getConfirmDialog();
	 case FILESELECT:
	     return getFileSelect();
	 case CLICKITEM :
	     return getDialogClick();
	 case ABOUT:
	     return getDialogAbout();
	 }
	 return null;
     }

    private AlertDialog getClearConfirmDialog() {
	return new AlertDialog.Builder(anTimeLog.this)
	    .setTitle(res.getText(R.string.confirm))
	    .setMessage(res.getText(R.string.clear_confirm))
	    .setNegativeButton(res.getText(R.string.cancel), new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog,int which) {
		    }
		})
	    .setPositiveButton(res.getText(R.string.ok),new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog,int which) {
			anTimeLog.this.myDBHelper.clearall();
			anTimeLog.this.events.clear();
			anTimeLog.this.myAdapter.notifyDataSetChanged();
		    }
		}
		)
	    .create();
    }

    private AlertDialog getConfirmDialog() {
	return new AlertDialog.Builder(anTimeLog.this)
	    .setTitle(res.getText(R.string.confirm))
	    .setMessage(res.getText(R.string.recover_confirm))
	    .setNegativeButton(res.getText(R.string.cancel), new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog,int which) {
		    }
		})
	    .setPositiveButton(res.getText(R.string.ok),new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog,int which) {
			((anTimeLogApplication) anTimeLog.this.getApplication()).shutdownDataBase();
			BackupHelper.restoreDatabase(anTimeLog.this.seletedFile);
			anTimeLog.this.needReflash = true;
			anTimeLog.this.onResume();
		    }
		}
		)
	    .create();
    }

    private AlertDialog getFileSelect() {
	return new AlertDialog.Builder(anTimeLog.this)
	    .setTitle(res.getText(R.string.confirm))
	    
	    .setNegativeButton(res.getText(R.string.cancel), new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int which) {
			removeDialog(FILESELECT);
		    }
		})
	    .setItems(backupFile,new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int which) {
			anTimeLog.this.seletedFile =(String) anTimeLog.this.backupFile[which];
			showDialog(RECORVERYCONFIRM);
			removeDialog(FILESELECT);
		    }
		}).create();
    }

    private AlertDialog getDialogClick() {
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

    }
    private AlertDialog getDialogAbout() {
	LinearLayout aboutLabel = new LinearLayout(this);
	aboutLabel.setOrientation(1);
	LinearLayout.LayoutParams rlText = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
									 ViewGroup.LayoutParams.FILL_PARENT,2);

	final TextView message = new TextView(this);
	final SpannableString s = 
	    new SpannableString(res.getText(R.string.author_info));
	Linkify.addLinks(s, Linkify.ALL);
	message.setText(s);
	message.setMovementMethod(LinkMovementMethod.getInstance());
	message.setTextSize(19f);
	aboutLabel.addView(message,rlText);
	/*	LinearLayout.LayoutParams rlImage = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
									      ViewGroup.LayoutParams.FILL_PARENT,1);
	WebView wv = new WebView(this);
	wv.loadUrl("http://antimelog.appspot.com/ads.html");
	aboutLabel.addView(wv,rlImage);*/
	return new AlertDialog.Builder(anTimeLog.this)
	    .setTitle(R.string.about)
	    .setView(aboutLabel)
	    .create();
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
	menu.add(0,anTimeLog.CLEARALL, 0 , R.string.clearall).setIcon(android.R.drawable.ic_menu_close_clear_cancel);
	menu.add(0,anTimeLog.MENU_BACKUP, 0 , R.string.backup).setIcon(android.R.drawable.ic_menu_save);
	menu.add(0,anTimeLog.MENU_RECOVERY, 0 , R.string.recovery).setIcon(android.R.drawable.ic_menu_rotate);
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
	    //	    myDBHelper.clearall();
	    //this.events.clear();
	    //this.myAdapter.notifyDataSetChanged();
	    //	    BackupHelper.BackupDatabase();
	    showDialog(CLEARCONFIRM);
	    
	    return true;
	case anTimeLog.MENU_BACKUP:
	    String result = BackupHelper.BackupDatabase();
	    if("".equals(result)) {
		Toast.makeText(getApplicationContext(),res.getText(R.string.backupe),Toast.LENGTH_LONG).show();
	    } else {
		Toast.makeText(getApplicationContext(),result + " " + res.getText(R.string.backups),Toast.LENGTH_LONG).show();
	    }
	    needReflash = true;
	    return true;
	case anTimeLog.MENU_RECOVERY:
	    backupFile = BackupHelper.getBackupFileList();
	    if(backupFile.length == 0) {
		Toast.makeText(getApplicationContext(),res.getText(R.string.nobackupfile),Toast.LENGTH_LONG).show();
		return true;
	    }
	    showDialog(FILESELECT);
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
