import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;
import java.io.*;
import java.util.*;

public class WebParser{
    public static String gameId;
    
    public Roster ParseRoster(Document boxScore, String team)
    {
	Roster roster = new Roster();
	Elements players = boxScore.select("#"+team +"-team-batter, #"+team+"-team-pitcher").get(0).select("a");
	for ( int i = 0; i < players.size(); i++ ){
	    Element e = players.get(i);
	    roster.addPlayer(new Player(e.text(), getId(e)));
	}
	return roster;
    }
    public AtBat ParseAtBats(Document plays)
    {
	Elements atBats = plays.select(".plays-atbat");
	Element ab = atBats.get(0);
	
	AtBat firstAtBat = 
	    new AtBat(BatterName(ab), 
		      PitcherName(ab), 
		      GetResult(ab),
		      GetPitches(ab));

	return firstAtBat;
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
	

