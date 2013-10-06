import java.io.*;
import java.util.*;

public class AtBats{
    public Vector<AtBat> container;
    public AtBats()
    {
	container = new Vector<AtBat>();
    }
    public void Add(AtBat ab)
    {
	container.add(ab);
    }
    public AtBat Get(int index)
    {
	return container.get(index);
    }
    public void Print()
    {
	for ( AtBat ab : container )
	    {
		ab.Print();
	    }
    }
    public void PrintResults()
    {
	for ( AtBat ab : container )
	    {
		ab.result.Print();
	    }
    }	
}