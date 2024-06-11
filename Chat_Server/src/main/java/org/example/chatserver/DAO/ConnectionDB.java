package org.example.chatserver.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDB {
    private static Connection con;
    private static final String url="jdbc:mysql://localhost:3306/nh_chat_app";
    private static final String user="root";
    private static final String password="";
    public static Connection getConnectionDB() {
        try {
            con=DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return con;
    }
}
