package net.laihj.anTimeLog;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Button;
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

import net.laihj.anTimeLog.eventItem;
import net.laihj.anTimeLog.eventAdapter;
import net.laihj.anTimeLog.DBHelper;

import java.util.ArrayList;
import java.util.Date;

public class anTimeLog extends Activity
{
    final static private int LONGCLICK = 1;
    final static private int SETTINGS = 2;
    final static private int ABOUT = 3;
    final static private int MENU_SETTING = Menu.FIRST;
    final static private int MENU_ALA = Menu.FIRST + 1;
    private ArrayList<eventItem> events;
    private eventAdapter myAdapter;
    private DBHelper myDBHelper;
    private AutoCompleteTextView quickText ;
    public ListView list;
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

    private String [] temps = { "吃饭","上班的车","CQ","读书" };
    private String [] tempType = { "life","tra","work","read" };
     @Override
    protected Dialog onCreateDialog(int id) {
	 switch( id ) {
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
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case anTimeLog.MENU_SETTING:
                    Intent intent = new Intent("net.laihj.anTimeLog.action.SETTING");
		    startActivityForResult(intent,SETTINGS);
                return true;
        }
        return super.onMenuItemSelected(featureId, item);
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
