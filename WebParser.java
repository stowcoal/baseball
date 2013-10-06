import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;
import java.io.*;
import java.util.*;

public class WebParser{
    public static String gameId;
    public WebParser(String gid)
    {
	gameId = gid;
    }
    public Roster ParseRoster(String team)
    {
	Connection box = Jsoup.connect("http://mlb.mlb.com/mlb/gameday/index.jsp?gid=" + gameId + "&mode=box");
	Roster roster = new Roster();
	try{
	    Document boxScore = box.get();
	    Elements players = boxScore.select("#"+team +"-team-batter, #"+team+"-team-pitcher").get(0).select("a");
	    for ( int i = 0; i < players.size(); i++ ){
		Element e = players.get(i);
		roster.addPlayer(new Player(e.text(), getId(e)));
	    }
	    return roster;
	}
	catch (IOException e)
	    {
		System.err.println("Error");
		return null;
	    }

    }
    public AtBats ParseAtBats()
    {
	Connection plays = Jsoup.connect("http://mlb.mlb.com/mlb/gameday/index.jsp?gid=" + gameId + "&mode=plays");
	try{
	    AtBats abs = new AtBats();
	    Document playByPlay = plays.get();
	    Elements atBats = playByPlay.select(".plays-atbat");
	    for ( Element ab : atBats )
		{
		    abs.Add(new AtBat(BatterName(ab), 
				      PitcherName(ab), 
				      GetResult(ab),
				      GetPitches(ab)));
		}
	    return abs;
	}
	catch (IOException e)
	    {
		System.err.println("Error");
		return null;
	    }
    }
    private Integer getId(Element e)
    {
	return Integer.parseInt(e.attr("href").split("=")[1]);
    }
    public String BatterName(Element ab)
    {
	return ab.select(".plays-atbat-batter > strong").get(0).text();
    }
    public String PitcherName(Element ab)
    {
	return ab.select(".plays-atbat-pitcher > strong").get(0).text();
    }
    private Result GetResult(Element ab)
    {
	return new Result(ab.select("dt").get(0).ownText());
    }
    private Vector<Pitch> GetPitches(Element ab)
    {
	Elements pitchTypes = ab.select(".plays-pitch-pitch");
	Elements pitchSpeeds = ab.select(".plays-pitch-speed");
	Elements pitchResults = ab.select(".plays-pitch-result");
	Vector<Pitch> pitches = new Vector<Pitch>();
	for(int i = 1; i < pitchTypes.size(); i++)
	    {
		pitches.add(new Pitch(pitchTypes.get(i).text(), Integer.valueOf(pitchSpeeds.get(i).text()), pitchResults.get(i).text()));
	    }
	return pitches;
    }
}
	

