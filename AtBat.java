import java.util.*;

public class AtBat{
    public String batter;
    public String pitcher;
    public Result result;
    public Vector<Pitch> pitches;
    public AtBat(String b, String p, Result r, Vector<Pitch> ps)
    {
	batter = b;
	pitcher = p;
	result = r;
	pitches = ps;
    }
    public void Print()
    {
	System.out.println(batter);
	System.out.println(pitcher);
	result.Print();
	for (Pitch p: pitches)
	    p.Print();
    }
}