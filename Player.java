public class Player{
    public String firstName;
    public String firstPart;
    public String lastName;
    public Integer id;
    public Player(String n)
    {
	firstPart = "";
	firstName = "";
	lastName = "";
	if ( n.indexOf(',') == -1 )
	{
  	    lastName = n;
	}
	else {
	    firstPart = n.split(", ")[1];
	    lastName  = n.split(", ")[0];
	}
    }
    public Player(String n, Integer pid)
    {
	firstPart = "";
	firstName = "";
	lastName = "";
	if ( n.indexOf(',') == -1 )
	{
	    lastName = n;
	}
	else {
	    firstPart = n.split(", ")[1];
	    lastName  = n.split(", ")[0];
	}
	id = pid;
    }
    public void Print()
    {
	System.out.println(firstPart + ' ' + lastName + ' ' + id); 
    }
    public String FirstName()
    {
	return firstName;
    }
    public String LastName()
    {
	return lastName;
    }
}