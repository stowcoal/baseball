public class Result{
    private String desc;
    private Action action;
    
    public Result(String d)
    {
	desc = d;
	switch (getAction()) {
	case "singles":
	    action = Action.SINGLE;
	    break;
	case "doubles":
	    action = Action.DOUBLE;
	    break;
	case "triples":
	    action = Action.TRIPLE;
	    break;
	case "homers":
	    action = Action.HOMERUN;
	    break;
	case "walks.":
	    action = Action.WALK;
	    break;
	case "hit":
	    action = Action.HITBYPITCH;
	    break;
	case "grounds":
	    action = Action.GROUNDOUT;
	    break;
	case "flies":
	    action = Action.FLYOUT;
	    break;
	case "lines":
	    action = Action.LINEOUT;
	    break;
	case "pops":
	    action = Action.POPOUT;
	    break;
	case "strikes":
	    action = Action.KSWINGING;
	    break;
	case "called":
	    action = Action.KLOOKING;
	    break;
	case "out":
	    action = Action.SACRIFICE;
	    break;
	default:
	    action = Action.UNKNOWN;
	    break;
	}
    }
    public void Print()
    {
	System.out.println(action);
    }
    private String getAction()
    {
	String[] words = desc.split(" ");
	for (String s : words)
	    {
		if (!Character.isUpperCase(s.charAt(0)))
		    return s;
	    }
	return null;
    }
    public enum Action {
	SINGLE, DOUBLE, TRIPLE, HOMERUN, WALK, HITBYPITCH, ERROR, 
	    GROUNDOUT, FLYOUT, LINEOUT, POPOUT, KSWINGING, KLOOKING, SACRIFICE, UNKNOWN;
	    } 

}