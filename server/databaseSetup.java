import java.sql.*;
public class databaseSetup {
	
	public static String username;
	public static String password;
	public static Connection connection;
	public static String url;
	public static String databaseName;
	
	public boolean setUser(String name, String pass, String database, String port) {
		username=name;
		password=pass;
		databaseName=database;
		try {
			url="jdbc:mysql://localhost:"+port+"/"+databaseName;
			connection = DriverManager.getConnection(url,username,password);
		} catch (SQLException e) {
			return false;
		}
		return true;
	}
	
	public boolean checkDBExists(String dbName) {
		try {
			connection = DriverManager.getConnection(url, username, password);
		    ResultSet resultSet = connection.getMetaData().getCatalogs();  
		    while (resultSet.next()) {    
		    	String databaseName = resultSet.getString(1);
		        if(databaseName.equals(dbName)){
		        	return true;
		        }
		     }
		    resultSet.close();
		} catch (SQLException e) {
			return false;
		}
		return false;
	}
	
	public boolean checkTableExist(String table) throws SQLException {
		ResultSet rs = connection.getMetaData().getTables(null, null, table, null);
		while (rs.next()) { 
            String tableName = rs.getString("TABLE_NAME");
            if (tableName != null && tableName.equals(table)) {
                return true;
            }
        }
		return false;
	}

	public static void main(String[] args) throws SQLException {
		
		String databaseName="redditalk";
		System.out.println("Assigned database name: "+databaseName);
		
		String port="3306";
		System.out.println("Assigned port to database: "+port);
		
		String name="root";
		System.out.println("Assigned username from MySQL: "+name);
		
		String pass="toor";
		System.out.println("Assigned password from MySQL: "+pass);
		
		databaseSetup database = new databaseSetup();
		
		System.out.println("\nChecking connection with database...");
		if(database.setUser(name,pass,databaseName,port)) {
			System.out.println("Successfully connected to database...\n");
		}
		else {
			System.out.println("Error while connecting to database...");
			if(database.checkDBExists(databaseName)) {
				System.out.println("Database exists...  Error while setting up username, password or port number... Please retry process...\n");
				return;
			}
			else {
				System.out.println("Creating new database...");
				
				url="jdbc:mysql://localhost:"+port+"/";
				connection = DriverManager.getConnection(url,name,pass);
				
				Statement stmt = connection.createStatement();	
			    String sql = "CREATE DATABASE "+databaseName;
			    stmt.executeUpdate(sql);
			    
			    System.out.println("Database created successfully...");
			    if(database.setUser(name,pass,databaseName,port)) {
			    	System.out.println("Successfully connected to database...\n");
			    }
			    else {
			    	System.out.println("Error still exists please retry....\n");
			    	return;
			    }
			}
		}
		
		url="jdbc:mysql://localhost:"+port+"/"+databaseName;
		
		System.out.println("Checking existence of user_value table...");
		if(database.checkTableExist("user_value")) {
			System.out.println("user_value table already exists...\n");
		}
		else {
			System.out.println("user_value table does not exists..."); 
			System.out.println("Creating user_value table in database...");
			
			connection = DriverManager.getConnection(url,name,pass);
			
			Statement stmt = connection.createStatement();
		    String sql = "CREATE TABLE user_value("
		    		+ "user_id bigint unsigned NOT NULL UNIQUE AUTO_INCREMENT,"
		    		+ "username varchar(60) PRIMARY KEY,"
		    		+ "password varchar(30) NOT NULL,"
		    		+ "created_at timestamp default current_timestamp"
		    		+ ");";
		    stmt.executeUpdate(sql);
		    
		    System.out.println("Successfully created user_value table...\n");
		}
		
		System.out.println("Checking existence of topic_value table...");
		if(database.checkTableExist("topic_value")) {
			System.out.println("topic_value table already exists...\n");
		}
		else {
			System.out.println("topic_value table does not exists...");
			System.out.println("Creating topic_value table in database...");
			
			url="jdbc:mysql://localhost:"+port+"/"+databaseName;
			connection = DriverManager.getConnection(url,name,pass);

			Statement stmt = connection.createStatement();
		    String sql = "CREATE TABLE topic_value ("
		    		+ "topic_id bigint unsigned NOT NULL UNIQUE AUTO_INCREMENT ,"
		    		+ "topicname varchar(30) PRIMARY KEY,"
		    		+ "created_at timestamp default current_timestamp"
		    		+ ");";   
		    stmt.executeUpdate(sql);
		    
		    System.out.println("Successfully created topic_value table...\n");
		}
		
		System.out.println("Checking existence of topic_users table...");
		if(database.checkTableExist("topic_users")) {
			System.out.println("topic_users table already exists...\n");
		}
		else {
			System.out.println("topic_users table does not exists...");
			System.out.println("Creating topic_users table in database...");
			
			Statement stmt = connection.createStatement();
		    String sql = "CREATE TABLE topic_users ("
		    		+ "topicname varchar(30),"
		    		+ "username varchar(60),"
		    		+ "joined_at timestamp default current_timestamp,"
		    		+ "left_group int unsigned DEFAULT 0,"
		    		+ "FOREIGN KEY (topicname) REFERENCES topic_value(topicname),"
		    		+ "FOREIGN KEY (username) REFERENCES user_value(username)"
		    		+ ");";
		    stmt.executeUpdate(sql);
		    
		    System.out.println("Successfully created topic_users table...\n");
		}
		
		System.out.println("Checking existence of topic_mess table...");
		if(database.checkTableExist("topic_mess")) {
			System.out.println("topic_mess table already exists...\n");
		}
		else {
			System.out.println("topic_mess table does not exists...");
			System.out.println("Creating topic_mess table in database...");
			
			Statement stmt = connection.createStatement();
		    String sql = "CREATE TABLE topic_mess("
		    		+ "mess_id bigint unsigned AUTO_INCREMENT,"
		    		+ "topicname varchar(30),"
		    		+ "username varchar(60),"
		    		+ "message TEXT,"
		    		+ "send_at timestamp default current_timestamp,"
		    		+ "PRIMARY KEY(mess_id),"
		    		+ "FOREIGN KEY (topicname) REFERENCES topic_users(topicname),"
		    		+ "FOREIGN KEY (username) REFERENCES topic_users(username)"
		    		+ ");";
		    stmt.executeUpdate(sql);
		    
		    System.out.println("Successfully created topic_mess table...\n");
		}
		
		System.out.println("Checking existence of direct_mess table...");
		if(database.checkTableExist("direct_mess")) {
			System.out.println("direct_mess table already exists...\n");
		}
		else {
			System.out.println("direct_mess table does not exists...");
			System.out.println("Creating direct_mess table in database...");
			
			Statement stmt = connection.createStatement();
		    String sql = "CREATE TABLE direct_mess("
		    		+ "mess_id bigint unsigned AUTO_INCREMENT,"
		    		+ "sender_name varchar(60),"
		    		+ "reciever_name varchar(60),"	
		    		+ "message TEXT,"
		    		+ "send_at timestamp default current_timestamp,"
		    		+ "PRIMARY KEY (mess_id),"
		    		+ "FOREIGN KEY (sender_name) REFERENCES user_value(username),"
		    		+ "FOREIGN KEY (reciever_name) REFERENCES user_value(username)"
		    		+ ");";
		    stmt.executeUpdate(sql);
		    
		    System.out.println("Successfully created user_mess table...\n");
		}
		
		System.out.println("DATABASE SETUP SUCCESSFULL...");
	}

}
