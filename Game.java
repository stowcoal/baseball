import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;
import java.io.*;

public class Game{
    public static String gameId;
    public static Roster home;
    public static Roster away;
    public static void main(String[] args){
	gameId = new String("2013_09_17_slnmlb_colmlb_1");
	Connection plays = Jsoup.connect("http://mlb.mlb.com/mlb/gameday/index.jsp?gid=" + gameId + "&mode=plays");
	Connection box = Jsoup.connect("http://mlb.mlb.com/mlb/gameday/index.jsp?gid=" + gameId + "&mode=box");
	try{
	    Document boxScore = box.get();
	    away = new Roster(boxScore.select("#away-team-batter"));
	    home = new Roster(boxScore.select("#home-team-pitcher"));
	}
	catch (IOException e)
	    {
		System.err.println("Error");
	    }
	try{
	    Document playByPlay = plays.get();
	    Elements atBats = playByPlay.select(".plays-atbat");
	    AtBat ab = new AtBat(atBats.get(0), home, away);
	}
	catch (IOException e)
	    {
		System.err.println("Error");
	    }
    }
}