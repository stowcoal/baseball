import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;
import java.io.*;
import java.util.*;
import com.google.gson.*;

public class WebParser{
    public static String gameId;
    public WebParser(String gid)
    {
	gameId = gid;
    }
    public void ParseScoreboard(String date)
    {
	System.out.println(date);
	Connection conn = Jsoup.connect("http://mlb.mlb.com/components/schedule/schedule_" + date + ".json");
	try{
	    Document scoreboard = conn.get();
	    String json = scoreboard.text();
	    String[] gameIds = json.split("game_id\": \"");
	    for (String s : gameIds){
		System.out.println(s.substring(0, s.indexOf("\""))
				   .replace("/","_").replace("-", "_"));
	    }
	    Elements scores = scoreboard.select("#gameContainer");
	    for ( int i = 0; i < scores.size(); i++ ){
		Element e = scores.get(i);
		System.out.println(e.text());
	    }
	} catch  (IOException e){
	    System.err.println("Error");
	}
    }
    public Roster ParseRoster(String team)
    {
	Connection box = Jsoup.connect("http://mlb.mlb.com/mlb/gameday/index.jsp?gid=" + gameId + "&mode=box");
	Roster roster = new Roster();
	try{
	    Document boxScore = box.get();
	    Elements players = boxScore.select("#"+team +"-team-batter").get(0).select("a");
	    for ( int i = 0; i < players.size(); i++ ){
		Element e = players.get(i);
		roster.addPlayer(new Player(e.text(), getId(e)));
	    }
	    players = boxScore.select("#"+team+"-team-pitcher").get(0).select("a");
	    for ( int i = 0; i < players.size(); i++ ){
		Element e = players.get(i);
		roster.addPlayer(new Player(e.text(), getId(e)));
	    }

	    String teamId = boxScore.select("tr." + team + " > th > div").text();
	    roster.id = teamId;
	    Database db = new Database();
	    db.Execute("INSERT INTO teams (short_name) values ('" + teamId + "') ON DUPLICATE KEY UPDATE id = id");
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
	    Elements atBats = playByPlay.select(".plays-atbat, .plays-action");
	    Vector<Event> events = new Vector<Event>();
	    for ( Element ab : atBats )
	    {
		if ( ab.hasClass("plays-action") )
		{
		    events.add(new Event(ab.text()));
		}
		else
		{
		    events.add(new Event(ab.select("dt").get(0).ownText()));
		    abs.Add(new AtBat(BatterName(ab), 
				      PitcherName(ab), 
				      GetPitches(ab),
				      new Vector<Event>(events))); //using events will clear it from memory before abs is returned -- Java sucks
		    events.clear();
		}
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
    private Vector<Pitch> GetPitches(Element ab)
    {
	Elements pitchTypes = ab.select(".plays-pitch-pitch");
	Elements pitchSpeeds = ab.select(".plays-pitch-speed");
	Elements pitchResults = ab.select(".plays-pitch-result");
	Vector<Pitch> pitches = new Vector<Pitch>();
	for(int i = 1; i < pitchTypes.size(); i++)
	    {
		Integer pitchSpeed = 0;
		String pitchSpeedText = pitchSpeeds.get(i).text();
		if (pitchSpeedText != null && !pitchSpeedText.isEmpty())
		{
		    pitchSpeed = Integer.valueOf(pitchSpeedText);
		}
		pitches.add(new Pitch(pitchTypes.get(i).text(), pitchSpeed, pitchResults.get(i).text()));
	    }
	return pitches;
    } 
}
