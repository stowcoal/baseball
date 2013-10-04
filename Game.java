import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;
import java.io.*;

public class Game{
    public static String gameId;
    public static Roster home;
    public static Roster away;
    public static void main(String[] args){
	WebParser wp = new WebParser();
	if ( args.length > 0 )
	    gameId = args[0];
	else
	    gameId = new String("2013_10_03_pitmlb_slnmlb_1");
	Connection plays = Jsoup.connect("http://mlb.mlb.com/mlb/gameday/index.jsp?gid=" + gameId + "&mode=plays");
	Connection box = Jsoup.connect("http://mlb.mlb.com/mlb/gameday/index.jsp?gid=" + gameId + "&mode=box");
	try{
	    Document boxScore = box.get();
	    home = wp.ParseRoster(boxScore, "home");
	    away = wp.ParseRoster(boxScore, "away");
	}
	catch (IOException e)
	    {
		System.err.println("Error");
		return;
	    }
	try{
	    Document playByPlay = plays.get();
	    AtBat first = wp.ParseAtBats(playByPlay);
	    first.Print();
	    //GetPlayer(first.batter).Print();
	}
	catch (IOException e)
	    {
		System.err.println("Error");
		return;
	    }
    }
    private static Player GetPlayer(String name){
	Player p = home.getPlayerByName(name);
	if ( p == null )
	    p = away.getPlayerByName(name);
	return p;
    }
}