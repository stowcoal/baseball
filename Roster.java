import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;
import java.io.*;
import java.util.*;
public class Roster{
    public Set<Player> list;
    public String id;
    public int finalScore;
    public Roster()
    {
	list = new HashSet<Player>();
    }
    public void addPlayer(Player p)
    {
	list.add(p);
	Database db = new Database();
	db.Execute("INSERT INTO players (id, first_name_short, last_name) values (" + p.id + ", '" + p.firstPart + "', '" + p.lastName.replace("'", "\\'") + "') ON DUPLICATE KEY UPDATE first_name_short = '" + p.firstPart + "';");
    }
    public int size(){
	return list.size();
    }
    public Player getPlayerByName(String name)
    {
	String lastName = name.substring(name.indexOf('.') + 2 );
	for (Player p : list){
	    if ( p.LastName().equals(lastName) )
		return p;
	}
	return null;
    }
    public void Print()
    {
	System.out.println(id);
	for (Player p : list){
	    p.Print();
	}
    }
}
