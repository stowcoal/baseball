import java.io.*;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database{
    public String connString;
    public ResultSet rs;
    public Database()
    {
	try {
	    BufferedReader br = new BufferedReader(new FileReader("../config.ini"));
	    connString = br.readLine();
	    try {
		Class.forName("com.mysql.jdbc.Driver").newInstance();
	    } catch (Exception ex) {
		System.out.println("Could not register");
	    }
	} catch (Exception ex) {
	    System.out.println("Can't open that file, man");
	}
    }
    public void RunQuery(String query)
    {	
	try {
	    Connection conn = DriverManager.getConnection(connString);
	    Statement statement = conn.createStatement();
	    rs = statement.executeQuery(query);
	    statement.close();
	    conn.close();
	} catch (SQLException ex) {
	    System.out.println("SQLException: " + ex.getMessage());
	    System.out.println("SQLState: " + ex.getSQLState());
	    System.out.println("VendorError: " + ex.getErrorCode());
	}
    }
    public void CloseRS()
    {
	try {
	    rs.close();
	} catch(Exception e){}
    }
    public void PrintValues()
    {
        try {
	    while (rs.next()) {
		System.out.println(rs.getString(1));
	    }
	} catch (Exception e) {}
    }
    public void Execute(String sql)
    {
	try {
	    Connection conn = DriverManager.getConnection(connString);
	    Statement statement = conn.createStatement();
	    statement.executeUpdate(sql);
	    statement.close();
	    conn.close();
	} catch (SQLException ex) {
	    System.out.println("SQLException: " + ex.getMessage());
	    System.out.println("SQLState: " + ex.getSQLState());
	    System.out.println("VendorError: " + ex.getErrorCode());
	    }
    }
}