package pro.xway;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SqliteHandler {
    private static Connection connection;

    private SqliteHandler() {
    }

    public static Connection getConnection() throws SQLException {
        if (connection == null) {
            connection = DriverManager.getConnection("jdbc:sqlite:main.db");
        }
        return connection;
    }

    public static void disconnect(){
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Statement getStatement() throws SQLException {
        return getConnection().createStatement();
    }
}
