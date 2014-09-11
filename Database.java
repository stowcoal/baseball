import java.io.*;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database{
    public String connString;
    public ResultSet rs;
    public Statement statement;
    public Connection conn;
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
	    conn = DriverManager.getConnection(connString);
	    statement = conn.createStatement();
	    rs = statement.executeQuery(query);
	} catch (SQLException ex) {
	    System.out.println("SQLException: " + ex.getMessage());
	    System.out.println("SQLState: " + ex.getSQLState());
	    System.out.println("VendorError: " + ex.getErrorCode());
	}
    }
    public void Next()
    {
	try {
	    rs.next();
	} catch(Exception e){
	    System.out.println(e);
	}
    }
    public void Close()
    {
	try {
	    rs.close();
	    statement.close();
	    conn.close();
	} catch(Exception e){
	    System.out.println(e);
	}
    }
    public String GetValue(String column)
    {
	String s = "";
	try {
	    s = rs.getString(column);
	} catch (Exception e) {
	    System.out.println(e);
	}
	return s;
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
	    conn = DriverManager.getConnection(connString);
	    statement = conn.createStatement();
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