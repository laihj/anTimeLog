package net.laihj.anTimeLog;

public class reportItem {
    public String event;
    public Long seconds;

    public reportItem () { }

    public reportItem (String event, Long seconds) {
	this.event = event;
	this.seconds = seconds;
    }

    public String getTimeString() {
	Long sec = this.seconds;
	Long hours =  sec / ( 60 * 60 );
	sec = sec - hours * 3600 ;
	Long minute =  sec / 60 ;
	StringBuilder dura = new StringBuilder();
	if(hours > 0 ) {
	    dura.append( hours + " hr ");
	}
	if(minute > 0 ) {
	    dura.append( minute + " min" );
	}
	return dura.toString();
    }
}