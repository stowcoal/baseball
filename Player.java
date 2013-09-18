public class Player{
    private String firstName;
    private String lastName;
    private Integer id;
    public Player(String fn, String ln)
    {
	firstName = fn;
	lastName = ln;
    }
    public Player(String n)
    {
	firstName = n;
    }
    public void Print()
    {
	System.out.println(firstName + ' ' + lastName);
    }
}