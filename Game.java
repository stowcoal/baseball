import java.io.*;
import java.util.*;

public class Game{
    public String gameId;
    public Roster home;
    public Roster away;
    public AtBats plays;
    public Game(String g)
    {
	gameId = g;
	WebParser wp = new WebParser(gameId);
	home = wp.ParseRoster("home");
	away = wp.ParseRoster("away");
	plays = wp.ParseAtBats();
    }
    public void Analyze(Boolean writeToDatabase){
	Situation current = new Situation();
	for (AtBat ab : plays.container){
	    Player batter = GetPlayer(ab.batter);
	    Player pitcher = GetPlayer(ab.pitcher);
	    for (Event e : ab.events){
		e.before = new Situation(current);
		e.after = new Situation(current);
		if (e.equals(ab.events.lastElement()) ||
		    e.desc.indexOf("With") == 0){
		    e.after.ClearBases();
		    if (e.before.first != null){
			e.after = UpdateBase(e, e.before.first);
		    }
		    if (e.before.second != null){
			e.after = UpdateBase(e, e.before.second);
		    }
		    if (e.before.third != null){
			e.after = UpdateBase(e, e.before.third);
		    }
		    ab.action = GetAction(e.desc);
		    if (First(ab.action)){
			e.after.first = batter;			
		    }
		    else if (Second(ab.action)){
			e.after.second = batter;
		    }
		    else if (Third(ab.action)){
			e.after.third = batter;
		    }
		    else if (HomeRun(ab.action)){
			e.after.AddRuns(1);
		    }
		    else if (Out(ab.action) && !ForceOut(e.desc)){
			e.after.AddOuts(1);
		    }
		    e.after = AdvanceBatter(e, batter);
		    if (writeToDatabase){		
			e.WriteToDatabase(batter, pitcher, GetAction(e.desc), gameId);
		    }
		    if (e.after.outs == 3){
			e.after.NewInning();
		    }
		}
		else if(e.desc.indexOf("Pinch-runner") > 0){
		    e.after = PinchRunner(e);
		}
		current = new Situation(e.after);			
	    }
	}
    }
    public Situation PinchRunner(Event e){
	// ...Pinch-runner first last replaces first last.
	int start, end;
	Player pr = new Player();
	Player r = new Player();
	start = e.desc.indexOf(" Pinch-runner ") + 14;
	end   = e.desc.indexOf(" replaces ");
	if (start > -1 && end > start){
	    pr = GetPlayer(e.desc.substring(start, end));
	}
	start = e.desc.indexOf(" replaces ") + 10;
	end   = e.desc.indexOf(".");
	if (start > -1 && end > start){
	    r = GetPlayer(e.desc.substring(start, end));
	}
	if (pr.id != null && r.id != null){
	    e.after.SetPlayerBase(pr, e.after.GetPlayerBase(r));
	}
	
	return e.after;
    }
    public void Print()
    {
	for (AtBat ab : plays.container){
	    ab.Print();
	}
    }
    public Situation Final()
    {
	AtBat last = plays.container.lastElement();
	return last.events.lastElement().after;
    }
    public void PrintBoxScore()
    {
	for (AtBat ab : plays.container){
	    if (ab.Top()){
		System.out.println(away.id);
	    }
	    else{
		System.out.println(home.id);
	    }	    
	    GetPlayer(ab.batter).Print();
	    GetPlayer(ab.pitcher).Print();
	    System.out.println(ab.action);
	    ab.events.lastElement().after.Print();
	}
    }
    public void PrintBoxScoreToFile(String filename)
    {
	try {
	    PrintWriter pw = new PrintWriter(filename, "UTF-8");
	    for (AtBat ab : plays.container){
		if (ab.Top()){
		    pw.println(away.id);
		}
		else{
		    pw.println(home.id);
		}
		Player batter = GetPlayer(ab.batter);
		if (batter == null){
		    System.out.println(ab.batter);
		}

		Player pitcher = GetPlayer(ab.pitcher);

		if (pitcher == null){
		    System.out.println(ab.pitcher);
		}
		pw.println(batter.FullName());
		pw.println(pitcher.FullName());
		pw.println(ab.action);
		for (Event e : ab.events) {
		    if (e.equals(ab.events.lastElement()) ||
			e.desc.indexOf("With") == 0){			
			pw.println(e.desc);
			e.after.PrintToFile(pw);
		    }
		}
	    }	    
	    pw.close();
	} catch (Exception e) {
	    System.out.println(e);
	}

    }
    public void PrintRosters()
    {
	home.Print();
	away.Print();
    }
    public Situation AdvanceBatter(Event e, Player p)
    {
	int start = 0;
	int end = 0;
	if (p != null){
	    start = e.desc.indexOf(p.FullName(), (e.desc.indexOf(p.FullName()) + 1));
	    end   = e.desc.indexOf(".", start);
	}
	if (start > 0 && end > start){
	    String sub = e.desc.substring(start, end);
	    e.after.ClearBase(e.after.GetPlayerBase(p));
	    if (sub.indexOf("to 1st") > -1){
		e.after.first = p;
	    }
	    else if (sub.indexOf("to 2nd") > -1){
		e.after.second = p;
	    }
	    else if (sub.indexOf("to 3rd") > -1){
		e.after.third = p;
	    }
	    else if (sub.indexOf("scores") > -1){
		e.after.AddRuns(1);
	    }
	    else if (sub.indexOf("out at") > -1){
		e.after.AddOuts(1);
	    }
	}
	return e.after;
    }
    public Situation UpdateBase(Event e, Player p)
    {
	Integer base = e.before.GetPlayerBase(p);
	String sub = "";
	int startSub = e.desc.indexOf(p.FullName() + " advances");
	if (startSub == -1){
	    startSub = e.desc.indexOf(p.FullName()); 
	}

	int endSub   = e.desc.indexOf(".", startSub + p.FullName().length());
	if (startSub > 0 && endSub > startSub){
	    sub = e.desc.substring(startSub, endSub);
	    if (sub.indexOf("to 1st") > -1){
		e.after.first = p;
	    }
	    else if (sub.indexOf("to 2nd") > -1){
		e.after.second = p;
	    }
	    else if (sub.indexOf("to 3rd") > -1){
		e.after.third = p;
	    }
	    else if (sub.indexOf("steals") > -1){
		if (sub.indexOf("2nd base") > -1){
		    e.after.second = p;
		}
		else if (sub.indexOf("3rd base") > -1){
		    e.after.third = p;
		}
		else if (sub.indexOf("home") > -1){
		    e.after.AddRuns(1);
		}
	    }
	    else if (sub.indexOf("caught stealing") > -1){
		e.after.AddOuts(1);
	    }
	    else if (sub.indexOf("doubled off") > -1 || 
		     sub.indexOf(" at 1st") > -1 ||
		     sub.indexOf(" at 2nd") > -1 ||
		     sub.indexOf(" at 3rd") > -1 ||
		     sub.indexOf(" at home") > -1
		     ){
		e.after.AddOuts(1);
	    }
	    else if (sub.indexOf("scores") > -1){
		e.after.AddRuns(1);
	    }
	    else{
		e.after.SetPlayerBase(p, base);
	    }	
	}
	else{
	    e.after.SetPlayerBase(p, base);
	}	

	return e.after;
    }
    private Player GetPlayer(String name){
	Player p = home.getPlayerByName(name);
	if ( p == null )
	    p = away.getPlayerByName(name);
	return p;
    }
    public Boolean Out(Action a)
    {
	if (a == Action.GROUNDOUT ||
	    a == Action.FLYOUT    ||
	    a == Action.LINEOUT   ||
	    a == Action.POPOUT    ||
	    a == Action.KSWINGING ||
	    a == Action.KLOOKING  ||
	    a == Action.SACRIFICE)
	    return true;
	else 
	    return false;
    }
    public Boolean ForceOut(String desc)
    {
	return desc.indexOf("force out") > 0;
    }
    public Boolean First(Action a)
    {
	return a == Action.SINGLE ||
	    a == Action.WALK || a == Action.INTENTIONALWALK || 
	    a == Action.HITBYPITCH || a == Action.ERROR || 
	    a == Action.FIELDERSCHOICE;
    }
    public Boolean Second(Action a)
    {
	return a == Action.DOUBLE;
    }	
    public Boolean Third(Action a)
    {
	return a == Action.TRIPLE;
    }
    public Boolean HomeRun(Action a)
    {
	return a == Action.HOMERUN;
    }
    private Boolean hasUpperCase(String s) {        
	for(int i=s.length()-1; i>=0; i--) {
	    if(Character.isUpperCase(s.charAt(i))) {
		return true;
	    }
	}
	return false;
    }
    private Action GetAction(String desc)
    {
	String[] words = desc.split(" ");
	for (String s : words)
	{
	    if (!hasUpperCase(s))
		{
		    switch (s) {
		    case "singles":
			return Action.SINGLE;
		    case "hits":
			if (desc.indexOf("grand slam") > -1){
			    return Action.HOMERUN;
			}
			else if (desc.indexOf("double") > -1){
			    return Action.DOUBLE;
			}
			else if (desc.indexOf("sacrifice") > -1){
			    return Action.SACFC;
			}
		    case "doubles":
			return Action.DOUBLE;
		    case "triples":
			return Action.TRIPLE;
		    case "homers":
			return Action.HOMERUN;
		    case "walks.":
			return Action.WALK;
		    case "intentionally":
			return Action.INTENTIONALWALK;
		    case "hit":
			return Action.HITBYPITCH;
		    case "ground":
		    case "grounds":
			if (desc.indexOf("force out") > -1){
			    return Action.FIELDERSCHOICE;
			}
			else if (desc.indexOf("double play") > -1 &&
				 desc.split(" out at ").length > 2){
			    //this is kinda confusing
			    //if there is a double play, but neither
			    //runner that is out is the batter then
			    //it will be recorded as a fielders choice
			    //and we will know to let the runner be on base
			    return Action.FIELDERSCHOICE;
			}
			else{
			    return Action.GROUNDOUT;
			}
		    case "flies":
			return Action.FLYOUT;
		    case "lines":
			return Action.LINEOUT;
		    case "pops":
			return Action.POPOUT;
		    case "bunt": //bunt pops out
			return Action.POPOUT;
		    case "strikes":
			if (desc.indexOf("to 1st") > -1){
			    return Action.DROPPEDTHIRDSTRIKE;
			}
			else {
			    return Action.KSWINGING;
			}
		    case "called":
			return Action.KLOOKING;
		    case "out":
			return Action.SACRIFICE;
		    case "reaches":
			if (desc.indexOf("error") > -1)
			    return Action.ERROR;
			else if (desc.indexOf("fielder's choice") > -1)
			    return Action.FIELDERSCHOICE;
			else if (desc.indexOf("catcher interference") > -1)
			    return Action.CATCHERINTERFERENCE;
		    case "challenged":
		    case "reviewed":
			if ( desc.indexOf(": ") > -1 )
			    return GetAction(desc.split(": ")[1]);
		    case "batting,":
			if ( desc.indexOf("steals") > -1 )
			    return Action.STEAL;
			if ( desc.indexOf("caught") > -1 )
			    return Action.CAUGHTSTEALING;		    
			if ( desc.indexOf("defensive") > -1 )
			    return Action.DEFINDIF;
			if ( desc.indexOf("wild") > -1 )
			    return Action.WILDPITCH;
			if (desc.indexOf("passed") > -1 )
			    return Action.PASSBALL;
			if (desc.indexOf("picks off") > -1)
			    return Action.PICKOFF;
			if (desc.indexOf("balk") > -1)
			    return Action.BALK;
			if (desc.indexOf("throwing error") > -1)
			    return Action.ERROR;
		    default:
			System.out.println(desc);
			return Action.UNKNOWN;
		    }
		}   
	}
	return Action.UNKNOWN;
    }
}