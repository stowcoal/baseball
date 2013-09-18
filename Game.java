import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;
import java.io.*;

public class Game{
    public static String gameId;
    public static void main(String[] args){
	gameId = new String("2013_08_09_chnmlb_slnmlb_1");
	Connection c = Jsoup.connect("http://mlb.mlb.com/mlb/gameday/index.jsp?gid=" + gameId + "&mode=plays");
	try{
	    Document d = c.get();
	    Elements atBats = d.select(".plays-atbat");
	    System.out.println(gameId);
	    Roster home = new Roster(gameId);
	    AtBat ab = new AtBat(atBats.get(0));
	}
	catch (IOException e)
	    {
		System.err.println("Error");
	    }
    }
}