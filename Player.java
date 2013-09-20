public class Player{
    private String firstName;
    private String lastName;
    private Integer id;
    public Player(String fn, String ln)
    {
	firstName = fn;
	lastName = ln;
    }
    public Player(String n, Integer pid)
    {
	if ( n.indexOf(',') == -1 )
	    lastName = n;
	else {
	    lastName = n.split(", ")[0];
	    firstName = n.split(", ")[1];
	}
	id = pid;
    }
    public void Print()
    {
	System.out.println(lastName + ' ' + id); 
    }
    public String LastName()
    {
	return lastName;
    }
    public String FirstName()
    {
	return firstName;
    }
}