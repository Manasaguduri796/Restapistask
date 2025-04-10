package org.sanketika.springbootproject1.utility;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgreSQLClient {
    private String url;
    private String username;
    private String password;
    private Connection connection;

    public PostgreSQLClient(String url, String username, String password) throws SQLException {
        this.url = url;
        this.username = username;
        this.password = password;
        this.connection = DriverManager.getConnection(url,username,password);
    }


    public Connection getConnection(){
        return connection;
    }
    public void Close() throws SQLException{
        if(connection !=null){
            connection.close();
        }

    }


}
