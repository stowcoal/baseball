import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

public class AtBat{
    public AtBat(Element ab)
    {
	String desc = ab.getElementsByClass("plays-atbat-description").get(0).html();
	System.out.println(desc);
    }
}