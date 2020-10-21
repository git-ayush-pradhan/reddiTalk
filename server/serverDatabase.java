import java.sql.*;

public class serverDatabase {
	databaseSetup database = new databaseSetup();
	
	private String url = "jdbc:mysql://localhost:3306/redditalk";
	private String username = "root";
	private String password = "toor";
	
	private Connection connection = null;
	private String sql = null;
	private PreparedStatement pstmnt = null;
	private Statement stmnt = null;
	private ResultSet resultSet = null;
	
	public int userExist (String name) {
		try {
			connection = DriverManager.getConnection(url,username,password);
			sql = "SELECT * from user_value where username = ?";
			pstmnt = connection.prepareStatement(sql);
			pstmnt.setString(1,name);
			resultSet = pstmnt.executeQuery();
			if(resultSet.next()) {
				return 1;
			}
		}
		catch (SQLException ignore) {}
		finally {
	        if (resultSet != null) try { resultSet.close(); } catch (SQLException ignore) {}
	        if (pstmnt != null) try { pstmnt.close(); } catch (SQLException ignore) {}
	        if (connection != null) try { connection.close(); } catch (SQLException ignore) {}

	    }
		return 0;
	}
	
	public void userSignup (String name, String pass) {
		try {
			connection = DriverManager.getConnection(url,username,password);
			sql = "INSERT INTO user_value (username, password) VALUES (?, ?)";
			pstmnt = connection.prepareStatement(sql);
			pstmnt.setString(1,name);
			pstmnt.setString(2,pass);
			pstmnt.executeUpdate();
		}	
		catch (SQLException ignore) {}
		finally {
	        if (pstmnt != null) try { pstmnt.close(); } catch (SQLException ignore) {}
	        if (connection != null) try { connection.close(); } catch (SQLException ignore) {}
	    }
	}
	
	public int userUpdate (String name, String pass, String newPass) {
		try {
			connection = DriverManager.getConnection(url,username,password);
			stmnt = connection.createStatement();
			sql = ("SELECT * FROM user_value");
			resultSet = stmnt.executeQuery(sql);
			while(resultSet.next()) { 
				 if(name.equalsIgnoreCase(resultSet.getString("username")) && pass.equalsIgnoreCase(resultSet.getString("password"))) {
					 String s = "update user_value set password=? where username=?";
					 pstmnt = connection.prepareStatement(s);
					 pstmnt.setString(1, newPass);
					 pstmnt.setString(2, name);
					 pstmnt.executeUpdate();
					 return 1;
				 }
			}
		}	
		catch (SQLException ignore) {}
		finally {
	        if (resultSet != null) try { resultSet.close(); } catch (SQLException ignore) {}
	        if (pstmnt != null) try { pstmnt.close(); } catch (SQLException ignore) {}
	        if (stmnt != null) try { stmnt.close(); } catch (SQLException ignore) {}
	        if (connection != null) try { connection.close(); } catch (SQLException ignore) {}
	    }
		return 0;
	}
	
	public int userLogin (String name, String pass) {
		try {
			connection = DriverManager.getConnection(url,username,password);
			stmnt = connection.createStatement();
			sql = ("SELECT * FROM user_value;");
			resultSet = stmnt.executeQuery(sql);
			while(resultSet.next()) { 
				 if(name.equalsIgnoreCase(resultSet.getString("username")) && pass.equalsIgnoreCase(resultSet.getString("password"))) {
					 return 1;
				 }
			}
		}	
		catch (SQLException ignore) {}
		finally {
	        if (resultSet != null) try { resultSet.close(); } catch (SQLException ignore) {}
	        if (stmnt != null) try { stmnt.close(); } catch (SQLException ignore) {}
	        if (connection != null) try { connection.close(); } catch (SQLException ignore) {}
	    }
		return 0;
	}
	
	public int topicExist(String name) {
		try {
			connection = DriverManager.getConnection(url,username,password);
			sql = "SELECT * from topic_value WHERE topicname = ?";
			pstmnt = connection.prepareStatement(sql);
			pstmnt.setString(1,name);
			resultSet = pstmnt.executeQuery();
			if(resultSet.next()) {
				return 1;
			}
		}
		catch (SQLException ignore) {}
		finally {
	        if (pstmnt != null) try { pstmnt.close(); } catch (SQLException ignore) {}
	        if (connection != null) try { connection.close(); } catch (SQLException ignore) {}
	    }
		return 0;
	}
	
	public void topicSignup(String name) {
		try {
			connection = DriverManager.getConnection(url,username,password);
			sql = "INSERT INTO topic_value (topicname) VALUES (?)";
			pstmnt = connection.prepareStatement(sql);
			pstmnt.setString(1,name);
			pstmnt.executeUpdate();
		}	
		catch (SQLException ignore) {}
		finally {
	        if (resultSet != null) try { resultSet.close(); } catch (SQLException ignore) {}
	        if (pstmnt != null) try { pstmnt.close(); } catch (SQLException ignore) {}
	        if (connection != null) try { connection.close(); } catch (SQLException ignore) {}
	    }
	}
	
	public void setTopicUser(String name, String topic) {
		try {
			connection = DriverManager.getConnection(url,username,password);
			sql = "INSERT INTO topic_users (user_id,topic_id)"
					+ "SELECT user_value.user_id, topic_value.topic_id"
					+ "FROM user_value,topic_value"
					+ "WHERE user_value.username = (?)"
					+ "AND topic_value.topicname = (?)";
			pstmnt = connection.prepareStatement(sql);
			pstmnt.setString(1,name);
			pstmnt.setString(2, topic);
			pstmnt.executeUpdate();
		}
		catch (SQLException ignore) {}
		finally {
	        if (resultSet != null) try { resultSet.close(); } catch (SQLException ignore) {}
	        if (pstmnt != null) try { pstmnt.close(); } catch (SQLException ignore) {}
	        if (connection != null) try { connection.close(); } catch (SQLException ignore) {}
	    }
	}
	
}
