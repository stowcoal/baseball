import java.net.*;
import java.io.*;

public class URLConnectionReader {
    public static void main(String[] args) throws Exception {
        URL oracle = new URL("http://espn.go.com/mlb/playbyplay?gameId=320716108&full=1&inning=0");
        URLConnection yc = oracle.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(
								     yc.getInputStream()));
        String inputLine = "";
	try{
	    FileWriter fstream = new FileWriter("game.html");
	    BufferedWriter out = new BufferedWriter(fstream);
	    while ((inputLine = in.readLine()) != null && !inputLine.contains("<table border=\"1\" width=\"100%\" class=\"mod-data\">"))
		out.write("");
	    out.write(inputLine);
	    out.close();
	}
	catch (Exception e){
	    System.err.println("Error: " + e.getMessage());
	}
	in.close();
	String lines[] = inputLine.split("<*>");
	try{
	    FileWriter gameData = new FileWriter("game2.html");
	    BufferedWriter out = new BufferedWriter(gameData);
	    Boolean junk = true;
	    for(String s: lines)
		{
		    if (junk)
			junk = !s.contains("mod-container");
		    if (!junk)
			{
			    out.write(s + '>');
			    out.newLine();
			}
		}
	    out.close();
	}
	catch (Exception e){
	    System.err.println("Error: " + e.getMessage());
	}

    }
}