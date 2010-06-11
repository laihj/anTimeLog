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

import net.laihj.anTimeLog.eventItem;
import net.laihj.anTimeLog.eventAdapter;
import net.laihj.anTimeLog.DBHelper;

import java.util.ArrayList;
import java.util.Date;
import java.lang.CharSequence;

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
    private Resources res;
    /** Called when the activity is first created. */
    /*    private final Handler handler = new Handler () {
	    @Override
	    public handlerMessage ( final Message msg ) {
		progressDialog.dismiss();
		if ( events == null || events.size() == 0 ) {
		}
		else {
		    
		}
	    }
	    }*/
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
	requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.antimelog);

	myDBHelper = new DBHelper(this);
	list = (ListView) findViewById(R.id.list);
	quickText = (AutoCompleteTextView) findViewById(R.id.quickAddText);
	Button quickButton = (Button) findViewById(R.id.quickAddButton);
        res = getResources();
	events = new ArrayList<eventItem> ();
	//get all list first
	Log.i("before getall","before getall");
       	events = (ArrayList<eventItem>) myDBHelper.getAll();
	myAdapter = new eventAdapter(this, events);
	Log.i("after getall","after getall");
	list.setAdapter(myAdapter);
	list.setSelection(events.size()-1);

	quickButton.setOnClickListener(new OnClickListener() {
		public void onClick(View v) {
		    eventItem ei = new eventItem(quickText.getText().toString(), new Date());
		    anTimeLog.this.addEvent(ei);

		}
	    });
	quickButton.setOnLongClickListener(new OnLongClickListener() {
		public boolean onLongClick(View v) {
		    showDialog(LONGCLICK);
		    Log.i("Longclick","LongClick");
		    return true;
		}
	    });

        
	list.setOnCreateContextMenuListener( new OnCreateContextMenuListener() {
		 public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		     menu.add(0, DO_IT, 0, "do it");
		     Log.i("do it","do it");
		 }
		
	    });
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
	    myAdapter.notifyDataSetChanged();
	    quickText.setText("");
    }


    public boolean onContextItemSelected(MenuItem item) {
	AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
	eventItem ei = new eventItem(events.get((int) info.id).event, new Date());
	ei.type = events.get((int)info.id).type;
	addEvent(ei);
	return true;
    }

    private String [] temps = { "a","b","CQ","c" };
    private String [] tempType = { "life","tra","work","read" };

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
		 Log.i("CLICKITEM",selectedEvent.event);
		 CharSequence [] conMenu;
		 conMenu = res.getTextArray(R.array.conmenu);
		 return new AlertDialog.Builder(anTimeLog.this)
		     .setTitle(selectedEvent.event)
		     .setNegativeButton("do nothing", new DialogInterface.OnClickListener() {
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
				     break;
				 case CON_EDIT:
				     Intent intent = new Intent("net.laihj.anTimeLog.action.EDIT_ITEM");
				     intent.putExtra("eventid",selectedEvent.id);
				     Log.i("eventid","" + selectedEvent.id);
				     anTimeLog.this.startActivityForResult(intent,1);
				     break;
				 case CON_DO_IT:
				     eventItem ei = new eventItem(selectedEvent.event, new Date());
				     ei.type = selectedEvent.type;
				     anTimeLog.this.addEvent(ei);
				     break;
				 case CON_DELETE:
				     myDBHelper.delete(selectedEvent.id);
				     anTimeLog.this.events.remove(selectedEvent);
				     anTimeLog.this.myAdapter.notifyDataSetChanged();
				     selectedEvent = null;
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
	 case LONGCLICK :

	     return	 new AlertDialog.Builder(anTimeLog.this)
			.setTitle("template")
			.setItems(temps, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
				    eventItem ei = new eventItem(temps[which].toString(), new Date());
				    ei.type = tempType[which].toString();
				    addEvent(ei);
				}
			    })
			.create();
	 }
	 return null;
     }
    @Override
    public void onResume() {
	super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, anTimeLog.MENU_SETTING, 0, R.string.setting).setIcon(
            android.R.drawable.ic_menu_more);
	menu.add(0, anTimeLog.MENU_ALA, 0, R.string.analysis);
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
        }
        return true;
    }


    
    @Override
	protected void onActivityResult(int requestCode, int resultCode,
					Intent data) {
	super.onActivityResult(requestCode,resultCode,data);
	Log.i("bb","onR" + requestCode);
	Log.i("re","ab "+ resultCode);
	switch ( resultCode ) {
	case 10:
	    Log.i("aa","Result");
	    Long editedEvent;
	    editedEvent = data.getExtras().getLong("eventid");
	    for(int i = 0 ; i < events.size() ;i ++) {
		if(events.get(i).id == editedEvent) {
		    myDBHelper.freshItem(events.get(i));
		    myAdapter.notifyDataSetChanged();
		    break;
		}
	    }
	    break;
	}
    }
}
