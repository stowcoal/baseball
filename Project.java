import java.io.*;
import java.sql.ResultSet;
import java.util.*;

public class Project{
    public static void main(String[] args)
    {
	WebParser wp = new WebParser();
	Vector<String> gameIds = wp.ParseScoreboard("20140808");
	for (String s : gameIds){
	    //check if game is in database
	    //if it is, don't run it
	    Game g = new Game(s);
	    System.out.println(s);
	    g.Analyze(false);
	    g.Final().Print();
	    g.PrintBoxScoreToFile("../games/" + s);
	    if (g.Final().homeScore != g.home.finalScore){
		System.out.println("Incorrect home score");
	    }
	    if (g.Final().awayScore != g.away.finalScore){
		System.out.println("Incorrect away score");
	    }
	    //}
    }
}
