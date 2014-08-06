import java.io.*;
import java.sql.ResultSet;
import java.util.*;

public class Project{
    public static void main(String[] args)
    {
	WebParser wp = new WebParser();
	Vector<String> gameIds = wp.ParseScoreboard("20141111");
	for (String s : gameIds){
	    System.out.println(s);
	}
	String gameId;
	if ( args.length > 0 )
	    gameId = args[0];
	else
	    gameId = new String("2014_05_04_slnmlb_chnmlb_1");
	Game g = new Game(gameId);
	//	g.Analyze(false);
	//g.PrintBoxScore();
    }
}
