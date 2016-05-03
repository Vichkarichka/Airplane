package Auxiliary;

/**
 * Created by Сергей on 03.05.2016.
 */
import java.sql.*;

public class ConnectionFactory {
    private static Connection conn;
    private static Statement statmt;

    public static Connection GetConnection()
    {
        try{
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite:C:\\git2\\Airplane\\src\\Resources\\ProfilesList.db");
            //System.exit(0);
            return conn;
        }catch(Exception e)//TODO: remove.
        {
            System.exit(0);
            return null;
        }
    }
}
