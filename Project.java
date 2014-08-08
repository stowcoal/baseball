import java.io.*;
import java.sql.ResultSet;
import java.util.*;

public class Project{
    public static void main(String[] args)
    {
	WebParser wp = new WebParser();
	Vector<String> gameIds = wp.ParseScoreboard("20140805");
	for (String s : gameIds){
	    Game g = new Game(s);
	    System.out.println(s);
	    g.Analyze(false);
	    g.PrintBoxScoreToFile("../games/" + s);
	}
    }
}
