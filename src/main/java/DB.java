import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

public class DB {

    private Logger logger = Logger.getLogger(DB.class.getName());

    public Connection connection;

    public void connect(String host, int port, String userName, String password, String dbName){

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            logger.severe("nem találom a mysql drivert");
            return;
        }

        try {
            String connString = "jdbc:mysql://" + host + ":" + port + "/" + dbName + "?useJDBCCompliantTimeZoneShift=true&serverTimezone=UTC";
            connection = DriverManager.getConnection(connString, userName, password);
            if (connection == null) {
                logger.severe("a kapcsolat null-t adott vissza");
            }
        }catch (SQLException ex){
            ex.printStackTrace();
            logger.severe("mysql kapcsolat nem jött létre");
            return;
        }
    }

    public void addTask(Task task){
        try {
            String sql = "INSERT INTO task(feladat, ki) VALUES(?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, task.task);
            statement.setString(2, task.whose);
            statement.execute();
        } catch (SQLException ex){
            ex.printStackTrace();
            logger.severe("nem sikerült új adatot felvinni az adatbázisba");
        }
    }

    public List<Task> listTask(){
        try {
            String sql = "SELECT * FROM task";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet result = statement.executeQuery();
            LinkedList<Task> taskResult = new LinkedList<>();
            while (result.next()){
                taskResult.add(new Task(result.getString(2), result.getString(3)));
            }
            return taskResult;
        } catch (SQLException ex){
            ex.printStackTrace();
        }

        return new LinkedList<>();
    }

   public void removeTask(int id){
        PreparedStatement statement = null;
        try {
            String sql = "DELETE FROM task WHERE id = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            statement.execute();
        } catch (SQLException ex){
            ex.printStackTrace();
            logger.severe("nem sikerült törülni az adatbázisból");
        }
    }

    public int getId(String task, String whose){
        try {
            String sql = "SELECT id FROM task WHERE feladat = ? AND ki = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, task);
            statement.setString(2, whose);
            ResultSet result = statement.executeQuery();
            if (result.next()){
                return result.getInt(1);
            }
        } catch (SQLException ex){
            ex.printStackTrace();
        }
        return 0;
    }

    public boolean getIdValidate(int id){
        try {
            String sql = "SELECT id FROM task WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();
            if (result.next()){
                return true;
            }
        }catch (SQLException ex){
            ex.printStackTrace();
        }

        return false;
    }
}
