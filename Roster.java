import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;
import java.io.*;
public class Roster{
    public Player[] roster;
    public Roster(Elements players)
    {
	roster = new Player[25];
	Elements playerElements = players.get(0).select("a");
	for ( int i = 0; i < playerElements.size(); i++ ){
	    Element e = playerElements.get(i);
	    roster[i] = new Player(e.text(), getId(e));
	}
    }
    private Integer getId(Element e)
    {
	return Integer.parseInt(e.attr("href").split("=")[1]);
    }
    public Player getPlayerByName(String name)
    {
	String lastName = name.split(". ")[1];
	for (int i = 0; i < 25 && roster[i] != null; i++){
	    //System.out.println(roster[i].LastName() + "==" + lastName);
	    if ( roster[i].LastName().equals(lastName) )
		return roster[i];
	}
	return null;
    }
}
