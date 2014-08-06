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
	connString = "jdbc:mysql://192.168.0.244/baseball?user=root&password=pascal";
	try {
	  Class.forName("com.mysql.jdbc.Driver").newInstance();
	} catch (Exception ex) {
	    System.out.println("Could not register");
	}

    }
    public void RunQuery(String query)
    {	
	try {
	    Connection conn = DriverManager.getConnection(connString);
	    Statement stmt = conn.createStatement();
	    rs = stmt.executeQuery(query);
	} catch (SQLException ex) {
	    System.out.println("SQLException: " + ex.getMessage());
	    System.out.println("SQLState: " + ex.getSQLState());
	    System.out.println("VendorError: " + ex.getErrorCode());
	}	
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
	    Statement stmt = conn.createStatement();
	    stmt.executeUpdate(sql);
	} catch (SQLException ex) {
	    System.out.println("SQLException: " + ex.getMessage());
	    System.out.println("SQLState: " + ex.getSQLState());
	    System.out.println("VendorError: " + ex.getErrorCode());
	}	
    }
}