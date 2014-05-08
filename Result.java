import java.util.*;

public class Result{
    public String desc;
    public Action action;
    public Situation after;

    public Result ()
    {
	after = new Situation();
    }
    public Result(String d)
    {
	desc = d;
	after = new Situation();
    }
    public Result(String d, Action a, Situation s)
    {
	desc = d;
	action = a;
	after = s;
    }
    /*    public Situation After(Situation s)
    {
	s.first = null;
	s.second = null;
	s.third = null;
	
	s.outs = s.outs + Outs();
	if ( s.outs == 3 ){
	    s.outs = 0;
	    s.first = null;
	    s.second = null;
	    s.third = null;
	    if ( s.top )
		s.top = false;
	    else {
		s.inning++;
		s.top = true;
	    }
	}
	return s;
	}*/
    public void Print()
    {
	System.out.println(desc + " " + action);
    }
    public int Outs()
    {
	if (desc.indexOf("double play") >= 0)
	    return 2;
	else if (desc.indexOf("triple play") >= 0)
	    return 3;
	else if (desc.indexOf("out") >= 0)
	    return 1;
	else 
	    return 0;
    }/*
     */

}