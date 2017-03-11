
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.rem.otdel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;
import javax.swing.JOptionPane;

/**
 *
 * @author tigler
 */
public class RepairMobile {

    public static Statement st;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        // TODO code application logic here
        //try {
            Locale.setDefault(Locale.ENGLISH);
            Class.forName("oracle.jdbc.OracleDriver");
            Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "REPAIRUSER", "1234");
            st = connection.createStatement();
            //connection.close();
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
       // } catch (Exception e) {
        //   ErrorLogin err = new ErrorLogin();
        //   err.setVisible(true);
        //}
        
    }

}
