import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;
import java.io.*;

public class PageParser{
    public static void main(String[] args){
	Connection c = Jsoup.connect("http://mlb.mlb.com/mlb/gameday/index.jsp?gid=2013_08_08_lanmlb_slnmlb_1&mode=plays");
	try{
	    Document d = c.get();
	    Elements atBats = d.body().getElementsByClass("plays-atbat");
	    AtBat ab = new AtBat(atBats.get(0));
	}
	catch (IOException e)
	    {
		System.err.println("Error");
	    }
    }
}