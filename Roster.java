import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;
import java.io.*;
import java.util.*;
public class Roster{
    public Set<Player> roster;
    public Roster(Elements players)
    {
	roster = new HashSet<Player>();
	Elements playerElements = players.get(0).select("a");
	for ( int i = 0; i < playerElements.size(); i++ ){
	    Element e = playerElements.get(i);
	    roster.add(new Player(e.text(), getId(e)));
	}
    }
    private Integer getId(Element e)
    {
	return Integer.parseInt(e.attr("href").split("=")[1]);
    }
    public Player getPlayerByName(String name)
    {
	String lastName = name.split(". ")[1];
	for (Player p : roster){
	    //System.out.println(roster[i].LastName() + "==" + lastName);
	    if ( p.LastName().equals(lastName) )
		return p;
	}
	return null;
    }
}
