/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package te4.nu.support;


import com.mysql.jdbc.Connection;
import java.sql.DriverManager;

/**
 *
 * @author albinarvidsson
 */
public class ConnectionFactory {
    public static Connection make(String Server) throws Exception{
        if (Server.equals("Server")) {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = (Connection) DriverManager.getConnection("jdbc:mysql://localhost/recept","root","");
            return connection;
        }
        return null;
    }
}
