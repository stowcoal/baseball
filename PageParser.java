import org.jsoup.*;
import org.jsoup.nodes.*;
import java.io.*;
public class PageParser{
    public static void main(String[] args){
	Connection c = Jsoup.connect("http://www.google.com");
	try{
	    Document d = c.get();
	    System.out.println(d.body().html());
	}
	catch (IOException e)
	    {
		System.err.println("Error");
	    }
    }
}