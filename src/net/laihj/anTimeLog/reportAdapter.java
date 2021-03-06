package net.laihj.anTimeLog;


import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.content.Intent;


import net.laihj.anTimeLog.reportItem;
import net.laihj.anTimeLog.reports;

import java.util.List;

public class reportAdapter extends BaseAdapter {

    private final Context context;
    private final List<reportItem> reports;

    public reportAdapter(Context context,List<reportItem> reports) {
	this.context = context;
	this.reports = reports;
    }

    public int getCount() {
	return this.reports.size();
    }

    public Object getItem(int Position) {
	return this.reports.get(Position);
    }

    public long getItemId(int Position) {
	return Position;
    }

    public View getView(int Position, View convertView, ViewGroup parent) {
	reportItem report = this.reports.get(Position);
	return new reportListView(this.context, report);
    }

    private class reportListView extends RelativeLayout {
        public reportItem myReport;
	private TextView event;
	private TextView theTime;
	private Context context;

	public reportListView(Context context, reportItem report) {
	    super(context);
            this.myReport = report;
	    this.context = context;
	    RelativeLayout.LayoutParams rlEvent = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
										  ViewGroup.LayoutParams.WRAP_CONTENT);
	    this.event = new TextView(context);
	    this.event.setId(1);
	    this.event.setText(myReport.event);
	    this.event.setTextSize(19f);
	    this.event.setPadding(2,8,2,8);
	    this.event.setMinWidth(150);
	    this.event.setMaxWidth(150);
	    this.event.setTextColor(Color.WHITE);
	    this.addView(this.event,rlEvent);

            final RelativeLayout.LayoutParams rltheTime = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
										  ViewGroup.LayoutParams.WRAP_CONTENT);
	    rltheTime.addRule(RelativeLayout.RIGHT_OF,1);
	    this.theTime = new TextView(context);
	    this.theTime.setId(2);
	    this.theTime.setText(report.getTimeString());
	    this.theTime.setTextSize(19f);
	    this.theTime.setPadding(2,8,2,8);
	    this.theTime.setTextColor(Color.WHITE);
	    this.addView(this.theTime,rltheTime);

	    setOnClickListener(new OnClickListener(){
		public void onClick(View v) {
		    if ("type".equals(((reports) reportListView.this.context).theType)) {
			return;
		    }
		    Intent reportIntent = new Intent("net.laihj.anTimeLog.action.REPORTDETAIL");
		    reportIntent.putExtra("event",reportListView.this.myReport.event);
		    reportIntent.putExtra("startDate",((reports) reportListView.this.context).getStartDate().getTime());
		    reportIntent.putExtra("endDate",((reports) reportListView.this.context).getEndDate().getTime());
		    ((reports) reportListView.this.context).startActivity(reportIntent);
		    }
		});
	}
    }
}