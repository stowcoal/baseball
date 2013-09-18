public class Pitch{
    public String type;
    public String result;
    public int speed; // mph
    public Pitch(String t, Integer s, String r)
    {
	type = t;
	result = r;
	speed = s;
    }
    public void Print()
    {
	System.out.println(String.format("%-25s%-5d%s", type, speed, result));
    }
}
