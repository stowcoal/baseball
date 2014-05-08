public class Event{
    public String desc;
    public Situation before;
    public Situation after;
    public Event(String d, Situation b, Situation a)
    {
	desc = d;
	before = b;
	after = a;
    }
    public Event(String d)
    {
	desc = d;
	before = new Situation();
	after = new Situation();
    }
    public void Print()
    {
	before.Print();
	System.out.println(desc);
    }
}