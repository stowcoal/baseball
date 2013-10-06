import java.io.*;

public class Game{
    public static String gameId;
    public static Roster home;
    public static Roster away;
    public static AtBats plays;
    public Game(Roster h, Roster a, AtBats p)
    {
	home = h;
	away = a;
	plays = p;
    }
    private static Player GetPlayer(String name){
	Player p = home.getPlayerByName(name);
	if ( p == null )
	    p = away.getPlayerByName(name);
	return p;
    }
}