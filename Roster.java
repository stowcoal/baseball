import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;
import java.io.*;
import java.util.*;
public class Roster{
    public Set<Player> roster;
    public Roster()
    {
	roster = new HashSet<Player>();
    }
    public void addPlayer(Player p)
    {
	roster.add(p);
    }
    public int size(){
	return roster.size();
    }
    public Player getPlayerByName(String name)
    {
	String lastName = name.split(". ")[1];
	for (Player p : roster){
	    if ( p.LastName().equals(lastName) )
		return p;
	}
	return null;
    }
}
