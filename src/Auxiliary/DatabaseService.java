package Auxiliary;

/**
 * Created by Сергей on 03.05.2016.
 */
import java.sql.*;
import Controllers.StartSceneController;

public class DatabaseService {
    public static void InsertPlayer(String name1, int score) {
        String str = Integer.toString(score);
        Connection connection;
        connection = ConnectionFactory.GetConnection();
        try {
            String query = " insert into Profiles (name, scores) values (?, ?)";
            PreparedStatement pst = connection.prepareStatement(query);
            pst.setString(1, name1);
            pst.setString(2, str);
            pst.execute();
        }
        catch(Exception e){e.printStackTrace();}
    }

}