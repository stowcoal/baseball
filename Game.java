import java.io.*;
import java.util.*;

public class Game{
    public String gameId;
    public Roster home;
    public Roster away;
    public AtBats plays;
    public Game(Roster h, Roster a, AtBats p, String g)
    {
	home = h;
	away = a;
	plays = p;
	gameId = g;
    }
    public void Analyze(){
	Integer first = 0;
	Integer second = 0;
	Integer third = 0;
	Situation current = new Situation();
	for (AtBat ab : plays.container)
	{
	    Player batter = GetPlayer(ab.batter);
	    Player pitcher = GetPlayer(ab.pitcher);
	    if (current.first != null)
		first = current.first.id;
	    else
		first = null;
	    if (current.second != null)
		second = current.second.id;
	    else
		second = null;
	    if (current.third != null)
		third = current.third.id;
	    else
		second = null;
	    for (Event e : ab.events){
		e.before = new Situation(current);
		e.after = new Situation(current);
		if (e.equals(ab.events.lastElement()) ||
		    e.desc.indexOf("With") == 0){
		    System.out.println(e.desc);
		    current.WriteToDatabase(batter, pitcher, GetAction(e.desc), gameId);
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
		    else if (Out(e.desc)){
			e.after.AddOuts(1);
		    }
		    else if (HomeRun(ab.action)){
			e.after.AddRuns(1);
		    }		    
		    current = new Situation(e.after);
		}
	    }
	}
    }
    public void Print()
    {
	for (AtBat ab : plays.container){
	    ab.Print();
	}
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
	    System.out.println(ab.batter);
	    GetPlayer(ab.pitcher).Print();
	    ab.events.lastElement().before.Print();
	    System.out.println(ab.action);
	    ab.events.lastElement().after.Print();
	}
    }
    public void PrintRosters()
    {
	home.Print();
	away.Print();
    }
    public Situation UpdateBase(Event e, Player p)
    {
	Integer base = e.before.GetPlayerBase(p);
	String sub = "";
	int startSub = e.desc.indexOf(p.LastName());
	int endSub   = e.desc.indexOf(".", startSub);
	if (startSub > -1 && endSub > startSub){
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
	    else if (sub.indexOf("scores") > -1){
		e.after.AddRuns(1);
	    }
	    else if (sub.indexOf("doubled off") > -1 || 
		     sub.indexOf("out at") > -1 ){
		e.after.AddOuts(1);
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
    public Boolean Out(String desc)
    {
	if (desc.indexOf("double play") >= 0)
	    return true;
	else if (desc.indexOf("triple play") >= 0)
	    return true;
	else if (desc.indexOf("out") >= 0)
	    return true;
	else 
	    return false;
    }
    public Boolean First(Action a)
    {
	return a == Action.SINGLE ||
	    a == Action.WALK || a == Action.INTENTIONALWALK || 
	    a == Action.HITBYPITCH;
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
    private Action GetAction(String desc)
    {
	String[] words = desc.split(" ");
	for (String s : words)
	{
	    if (!Character.isUpperCase(s.charAt(0)))
		{
		    switch (s) {
		    case "singles":
			return Action.SINGLE;
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
		    case "grounds":
			return Action.GROUNDOUT;
		    case "flies":
			return Action.FLYOUT;
		    case "lines":
			return Action.LINEOUT;
		    case "pops":
			return Action.POPOUT;
		    case "strikes":
			return Action.KSWINGING;
		    case "called":
			return Action.KLOOKING;
		    case "out":
			return Action.SACRIFICE;
		    case "reaches":
			return Action.ERROR;
		    case "challenged":
			if ( desc.indexOf(": ") >= 0 )
			    return GetAction(desc.split(": ")[1]);
		    case "batting,": 
			if ( desc.indexOf("caught") >= 0 )
			    return Action.CAUGHTSTEALING;		    
			if ( desc.indexOf("defensive") >= 0 )
			    return Action.DEFINDIF;
			if ( desc.indexOf("wild") >= 0 )
			    return Action.WILDPITCH;
			if (desc.indexOf("passed") >= 0 )
			    return Action.PASSBALL;
		    default:
			return Action.UNKNOWN;
		    }
		}   
	}
	return Action.UNKNOWN;
    }
}