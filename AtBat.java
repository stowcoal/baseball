import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

public class AtBat{
    private Player batter;
    private Player pitcher;
    public Result result;
    public AtBat(Element ab, Roster home, Roster away)
    {
	result = new Result(ab.select("dt").get(0).ownText());
	batter = away.getPlayerByName(BatterName(ab));
	pitcher = home.getPlayerByName(PitcherName(ab));
	batter.Print();
	pitcher.Print();
	Elements pitchTypes = ab.select(".plays-pitch-pitch");
	Elements pitchSpeeds = ab.select(".plays-pitch-speed");
	Elements pitchResults = ab.select(".plays-pitch-result");
	for(int i = 1; i < pitchTypes.size(); i++)
	    {
		Pitch p = new Pitch(pitchTypes.get(i).text(), Integer.valueOf(pitchSpeeds.get(i).text()), pitchResults.get(i).text());
		p.Print();
	    }
	result.Print();
    }
    public String BatterName(Element ab)
    {
	return ab.select(".plays-atbat-batter > strong").get(0).text();
    }
    public String PitcherName(Element ab)
    {
	return ab.select(".plays-atbat-pitcher > strong").get(0).text();
    }
}