package gb.esac.eventlist;


public class EventListException extends Exception {

    public EventListException () {
        super();
    }

    public EventListException (String msg) {
        super(msg);
    }

    public EventListException (String msg, Exception e) {
        super(msg+"\n", e);
    }
}
