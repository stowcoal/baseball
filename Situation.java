import java.io.*;

public class Situation {
    public int inning;
    public Boolean top;
    public Player first;
    public Player second;
    public Player third;
    public int homeScore;
    public int awayScore;
    public int outs;
    public Situation () {
	// default
	ClearBases();
	outs = 0;
	homeScore = 0;
	awayScore = 0;
	inning = 1;
	top = true;
    }
    public Situation( Situation s ){
	inning = s.inning;
	top = s.top;
	first = s.first;
	second = s.second;
	third = s.third;
	homeScore = s.homeScore;
	awayScore = s.awayScore;
	outs = s.outs;
    }
    public void Print() {
	if (second != null)
	    System.out.println(" *");
	else
	    System.out.println("");
	if ( third != null)
	    System.out.print("* ");
	else
	    System.out.print("  ");
	if ( first != null)
	    System.out.print("*");
	else
	    System.out.print(" ");
	if (top)
	    System.out.print("  top ");
	else
	    System.out.print("  bottom ");
	System.out.println(inning + " " + outs + " out home: " 
			   + homeScore + " away: " + awayScore);
    }
    public void WriteToDatabase(Player b, Player p, Action a, String gid)
    {
	//	System.out.println(b.lastName);
	String sql = "INSERT INTO events " +
	    "(pitcher_id, batter_id, first_id, second_id, third_id, outs, " +
	    "home_score, away_score, inning, result_id, hit_id, game_id) values " +
	    "("  + String.valueOf(p.id) + ", " + String.valueOf(b.id) +
	    ", " + String.valueOf(FirstId()) + ", " + String.valueOf(SecondId()) +
	    ", " + String.valueOf(ThirdId()) + ", " + String.valueOf(outs) + 
	    ", " + String.valueOf(homeScore) + ", " + String.valueOf(awayScore) +
	    ", " + String.valueOf(inning) + ", " + a.ordinal() + ", 0" + 
	    ", '" + gid + "');";
	Database db = new Database();
	//	System.out.println(sql);
	//     	db.Execute(sql);
    }
    public Integer GetPlayerBase(Player p)
    {
	if (first != null && first.id.equals(p.id))
	    return 1;
	else if (second != null && second.id.equals(p.id))
	    return 2;
	else if (third != null && third.id.equals(p.id))
	    return 3;
	else
	    return 0;
    }
    public void SetPlayerBase(Player p, Integer b)
    {
	if (b == 1)
	    first = p;
	else if (b == 2)
	    second = p;
	else if (b == 3)
	    third = p;
    }
    public Integer FirstId()
    {
	Integer id = 0;
	if (first != null)
	    id = first.id;
	return id;
    }
    public Integer SecondId()
    {
	Integer id = 0;
	if (second != null)
	    id = second.id;
	return id;
    }
    public Integer ThirdId()
    {
	Integer id = 0;
	if (third != null)
	    id = third.id;
	return id;
    }
    public void ClearBase(Integer b)
    {
	if (b == 1)
	    first = null;
	else if (b == 2)
	    second = null;
	else if (b == 3)
	    third = null;
    }	
    public void AddOuts(int o)
    {
	outs = outs + o;
	if ( outs == 3 ){
	    outs = 0;
	    ClearBases();
	    if ( top )
		top = false;
	    else {
		inning++;
		top = true;
	    }
	}
    }
    public void AddRuns(int r)
    {
	if ( top )
	    awayScore = awayScore + r;
	else
	    homeScore = homeScore + r;
    }
    public void ClearBases()
    {
	first = null;
	second = null;
	third = null;
    }
}