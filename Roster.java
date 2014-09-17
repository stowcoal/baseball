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
	Database db = new Database();
	String firstName = "";
	db.RunQuery("SELECT first_name FROM players WHERE id = " + p.id);
	if(db.Next()){
	    firstName = db.GetValue("first_name");
	}
	db.Close();
	if (firstName == null || firstName.equals("")){
	    WebParser wp = new WebParser();
	    p = wp.ParsePlayer(p);
	}
	else {
	    p.firstName = firstName;
	}
	if (p.firstName == null || p.firstName.equals("")){
	    System.out.println("** WARNING MISSING FIRST NAME: " + 
			       p.LastName() + " " + p.id + " **");
	}
	list.add(p);
	String sql = "INSERT INTO players (id, first_name, last_name) values (" + p.id + ", '" + p.firstName.replace("'", "\\'") + "', '" + p.lastName.replace("'", "\\'") + "') ON DUPLICATE KEY UPDATE first_name = '" + p.firstName + "', last_name = '" + p.lastName.replace("'", "\\'") + "';";
	db.Execute(sql);
    }
    public int size(){
	return list.size();
    }
    public Player getPlayerByName(String name)
    {
	int end = name.indexOf(".");
	String firstPart = "";
	if (end > -1){
	    firstPart = name.substring(0, end);
	}
	for (Player p : list){
	    if (name.endsWith(p.LastName()) && p.FirstName().startsWith(firstPart)){
		return p;
	    }
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
