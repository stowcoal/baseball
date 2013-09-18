import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;
import java.io.*;
public class Roster{
    public Player[] roster;
    public Roster(String gameId)
    {
	roster = new Player[25];
	Connection c = Jsoup.connect("http://mlb.mlb.com/mlb/gameday/index.jsp?gid=" + gameId + "&mode=box");
	try{
	    Document d = c.get();
	    Elements players = d.select("#home-team-batter a");
	    System.out.println(players.get(0).attr("href").split("=")[1]);
	}
	catch (IOException e)
	    {
		System.err.println("Error");
	    }
    }

}
