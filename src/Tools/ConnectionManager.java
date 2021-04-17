/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class ConnectionManager {
    
/******************************************************************************
 * La connection à la base de données en utilisant le design pattern Singleton*
 *****************************************************************************/
    
    private static Connection connection;
    private String url = "jdbc:mysql://localhost:3306/othello";
    private String user = "root";
    private String password = "";

    private ConnectionManager() throws SQLException, ClassNotFoundException {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, user, password);
    }

    public static Connection getConnection() throws SQLException, ClassNotFoundException {
            if(connection == null)
                    new ConnectionManager();
            return connection;
    }
}
