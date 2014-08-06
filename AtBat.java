import java.util.*;

public class AtBat{
    public String batter;
    public String pitcher;
    public Vector<Pitch> pitches;
    public Vector<Event> events;
    public Action action;
    public AtBat(String b, String p, Vector<Pitch> ps, Vector<Event> e)
    {
	batter = b;
	pitcher = p;
	pitches = ps;
	events = e;
    }
    public void Print()
    {
	System.out.println(batter);
	System.out.println(pitcher);
	for (Pitch p: pitches)
	{
	    p.Print();
	}
	for (Event e: events)
	{
	    e.Print();
	}
	System.out.println(action);
    }
    public Boolean Top()
    {
	return events.get(0).before.top;
    }
}