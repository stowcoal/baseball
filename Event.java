public class Event{
    public String desc;
    public Situation before;
    public Situation after;
    public Event(String d, Situation b, Situation a)
    {
	desc = d;
	before = b;
	after = a;
    }
    public Event(String d)
    {
	desc = d;
	before = new Situation();
	after = new Situation();
    }
    public void Print()
    {
	before.Print();
	System.out.println(desc);
    }
    public void WriteToDatabase(Player b, Player p, Action a, String gid)
    {
	String sql = "INSERT INTO events " +
	    "(pitcher_id, batter_id, first_id, second_id, third_id, outs, " +
	    "home_score, away_score, inning, first_id_after, second_id_after, " +
	    "third_id_after, outs_after, home_score_after, away_score_after, " +
	    "inning_after, result_id, hit_id, game_id) values " +
	    "("  + String.valueOf(p.id) + ", " + String.valueOf(b.id) +
	    ", " + String.valueOf(before.FirstId()) + 
	    ", " + String.valueOf(before.SecondId()) +
	    ", " + String.valueOf(before.ThirdId()) + 
	    ", " + String.valueOf(before.outs) + 
	    ", " + String.valueOf(before.homeScore) + 
	    ", " + String.valueOf(before.awayScore) +
	    ", " + String.valueOf(before.inning) + 
	    ", " + String.valueOf(after.FirstId()) + 
	    ", " + String.valueOf(after.SecondId()) +
	    ", " + String.valueOf(after.ThirdId()) + 
	    ", " + String.valueOf(after.outs) + 
	    ", " + String.valueOf(after.homeScore) + 
	    ", " + String.valueOf(after.awayScore) +
	    ", " + String.valueOf(after.inning) + 
	    ", " + a.ordinal() + ", 0" + ", '" + gid + "');";
	Database db = new Database();
	//System.out.println(sql);
	db.Execute(sql);
    }

}