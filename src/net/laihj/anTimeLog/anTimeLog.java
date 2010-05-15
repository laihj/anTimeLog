package net.laihj.anTimeLog;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.view.View;

import net.laihj.anTimeLog.eventItem;
import net.laihj.anTimeLog.eventAdapter;

import java.util.ArrayList;
import java.util.Date;

public class anTimeLog extends Activity
{
    private ArrayList<eventItem> events;
    private eventAdapter myAdapter;
    /** Called when the activity is first created. */
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
	requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

	ListView list = (ListView) findViewById(R.id.list);
	final EditText quickText = (EditText) findViewById(R.id.quickAddText);
	Button quickButton = (Button) findViewById(R.id.quickAddButton);

	events = new ArrayList<eventItem> ();
	myAdapter = new eventAdapter(this, events);

	list.setAdapter(myAdapter);

	quickButton.setOnClickListener(new OnClickListener() {
		public void onClick(View v) {
		    eventItem ei = new eventItem(quickText.getText().toString(), new Date());
		    events.add(ei);
		    myAdapter.notifyDataSetChanged();
		    quickText.setText("");
		}
	    });
    }
}
