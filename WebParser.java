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
    public WebParser()
    {
    }
    public Vector<String> ParseScoreboard(String date)
    {
	Vector<String> gameIds = new Vector<String>();
	Connection conn = Jsoup.connect("http://mlb.mlb.com/components/schedule/schedule_" + date + ".json");
	try{
	    Document scoreboard = conn.get();
	    String json = scoreboard.text();
	    String[] a = json.split("game_id\": \"");
	    for (String s : a){
		if (s.indexOf("\"") > 0){
		    gameIds.add(s.substring(0, s.indexOf("\""))
				.replace("/","_").replace("-", "_"));
		}
	    }
	    if (gameIds.size() > 0){ //remove the first garbage line [{
		gameIds.remove(0);
	    }
	   
	} catch  (IOException e){
	    System.err.println(e);
	}
	return gameIds;
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
	    roster.finalScore = Integer.parseInt( boxScore.select("." + team + " > .runs").get(0).text());
	}
	catch (IOException e){
	    System.err.println(e);
	}
	return roster;
    }
    public Player ParsePlayer(Player p)
    {
	Connection player = Jsoup.connect("http://mlb.mlb.com/team/player.jsp?player_id=" + p.id + "#gameType='R'");
	try{
	    Document playerInfo = player.get();
	    Elements name = playerInfo.select("#player_name");
	    if (name.size() > 0){
		String n = name.get(0).text();
		Integer end = n.indexOf(p.lastName);
		if (end > -1){
		    p.firstName = n.substring(0, n.indexOf(p.lastName) - 1);
		}
	    }
	}
	catch (IOException e){
	    System.err.println(e);
	}
	return p;
    }
    public AtBats ParseAtBats()
    {
	Connection plays = Jsoup.connect("http://mlb.mlb.com/mlb/gameday/index.jsp?gid=" + gameId + "&mode=plays");
	AtBats abs = new AtBats();
	try{
	    Document playByPlay = plays.get();

	    Integer awayFinal = Integer.parseInt( playByPlay.select(".away > .runs").get(0).text());
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
		    String desc = ab.select("dt").get(0).ownText();
		    if (events.size() > 0){
			if(events.lastElement().desc.equals(desc)){
			    events.remove(events.size() - 1);
			}
		    }
		    events.add(new Event(desc));
		    abs.Add(new AtBat(BatterName(ab), 
				      PitcherName(ab), 
				      GetPitches(ab),
				      new Vector<Event>(events))); //using events will clear it from memory before abs is returned -- Java sucks
		    events.clear();
		}
	    }
	}
	catch (IOException e){
	    System.err.println(e);
	}
	return abs;
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
