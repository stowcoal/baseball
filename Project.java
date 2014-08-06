import java.io.*;
import java.sql.ResultSet;

public class Project{
    public static void main(String[] args)
    {
	Database db = new Database();
	
	String gameId;
	if ( args.length > 0 )
	    gameId = args[0];
	else
	    gameId = new String("2014_05_04_slnmlb_chnmlb_1");
	WebParser wp = new WebParser(gameId);
	wp.ParseScoreboard("20140725");
	Game g = new Game(wp.ParseRoster("home"), 
			  wp.ParseRoster("away"), 
			  wp.ParseAtBats(),
			  gameId);
	//	g.Analyze();
	//	g.PrintBoxScore();
    }
}
