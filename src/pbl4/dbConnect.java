package pbl4;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class dbConnect {
	public static Connection GetConnection() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/data","root","");
		return conn;
	}

	public boolean Login(String username, String password) {
		// TODO Auto-generated method stub
		try {
			Connection con = GetConnection();
			PreparedStatement stmt = con.prepareStatement("select count(*) from user where username='?' and password='?'");
			stmt.setString(0, username);
			stmt.setString(1, password);
			int count = stmt.executeQuery().getInt(0);
			stmt.close();
			if(count == 1) return true;
			else return false;
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	public boolean Dangky(String username, String password) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
