import java.io.*;

public class Project{
    public static void main(String[] args)
    {
	String gameId;
	if ( args.length > 0 )
	    gameId = args[0];
	else
	    gameId = new String("2013_10_03_pitmlb_slnmlb_1");
	WebParser wp = new WebParser(gameId);

	Game g = new Game(wp.ParseRoster("away"), 
			  wp.ParseRoster("home"), 
			  wp.ParseAtBats());
	g.plays.PrintResults();
    }
}
