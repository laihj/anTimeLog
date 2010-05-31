package net.laihj.anTimeLog;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.view.View;
import android.util.Log;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;


import net.laihj.anTimeLog.eventItem;
import net.laihj.anTimeLog.eventAdapter;
import net.laihj.anTimeLog.DBHelper;

import java.util.ArrayList;
import java.util.Date;

public class anTimeLog extends Activity
{
    final static private int MENU_SETTING = Menu.FIRST;
    final static private int MENU_ALA = Menu.FIRST + 1;
    private ArrayList<eventItem> events;
    private eventAdapter myAdapter;
    private DBHelper myDBHelper;
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
	ListView list = (ListView) findViewById(R.id.list);
	final EditText quickText = (EditText) findViewById(R.id.quickAddText);
	Button quickButton = (Button) findViewById(R.id.quickAddButton);
        
	events = new ArrayList<eventItem> ();
	//get all list first
       	events = (ArrayList<eventItem>) myDBHelper.getAll();
	myAdapter = new eventAdapter(this, events);

	list.setAdapter(myAdapter);

	quickButton.setOnClickListener(new OnClickListener() {
		public void onClick(View v) {
		    eventItem ei = new eventItem(quickText.getText().toString(), new Date());
		    ei.id = myDBHelper.insert(ei);
		    events.add(ei);
		    myAdapter.notifyDataSetChanged();
		    quickText.setText("");
		}
	    });
    }
    @Override
    public void onResume() {
	super.onResume();
	Log.i("aa","resume");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, anTimeLog.MENU_SETTING, 0, "Setting").setIcon(
            android.R.drawable.ic_menu_more);
	menu.add(0, anTimeLog.MENU_ALA, 0, "ana");
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case anTimeLog.MENU_SETTING:
                Log.i("setting","menu click");
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
