package Main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    public static Connection con;
    public static DBConnection ins;

    private DBConnection(){}

    public static DBConnection getInstance(){
        if(ins==null){
            ins=new DBConnection();
        }
        return ins;
    }

    public static Connection makeConnection()  {
       try {
           if (con==null){
               con=DriverManager.getConnection("jdbc:mysql://localhost:3306/IT_Assert","root","pass@123");
           }
           return con;
       }catch (SQLException e){
           System.out.println(e.getMessage());
           return null;
       }
    }

}
