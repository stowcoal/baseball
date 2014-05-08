import java.util.*;

public class AtBat{
    public String batter;
    public String pitcher;
    public Vector<Pitch> pitches;
    public Vector<Event> events;
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
	System.out.println(events.size());
	for (Event e: events)
	{
	    e.Print();
	}
    }
}