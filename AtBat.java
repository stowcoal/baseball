import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

public class AtBat{
    private Player batter;
    private Player pitcher;
    public Result result;
    public AtBat(Element ab)
    {
	batter = GetBatter(ab);
	pitcher = GetPitcher(ab);
	result = new Result(ab.select("dt").get(0).ownText());
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
    private Player GetBatter(Element ab)
    {
	return new Player(ab.select(".plays-atbat-batter").get(0).ownText(), 1);
    }
    private Player GetPitcher(Element ab)
    {
	return new Player(ab.select(".plays-atbat-pitcher").get(0).ownText(), 1);
    }
}