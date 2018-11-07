package pro.xway;

import java.sql.*;
import java.util.Date;
import java.util.WeakHashMap;

/**
 * Класс управляет подключением к базе данных и создает шаблоны для PreparedStatement
 * @author Хайрутдинов Александр
 *
*/
public class SqliteHandler {
    private static Connection connection;
    private static int maxCounter;

    private SqliteHandler() {
    }

    /**
     * Метод создает Connection
     * @return возвращает Connection
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null) {
            synchronized (SqliteHandler.class) {
                connection = DriverManager.getConnection("jdbc:sqlite:main.db");
                Statement statement = SqliteHandler.connection.createStatement();
                statement.executeUpdate(
                        "CREATE TABLE if not exists intervals (" +
                                " id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                                " counter INT NOT NULL," +
                                " dateCreate DATE NOT NULL );");
                statement.close();
            }
        }
        Log.getInstance().getLogger(SqliteHandler.class).info("connection open");
        initCounter();

        return connection;
    }

    /**
     * Получаем и устанавливаем maxCounter
     * @throws SQLException
     */
    private static void initCounter() throws SQLException {
        ResultSet resultSet = connection.createStatement().executeQuery("SELECT MAX(counter) FROM intervals");
        resultSet.next();
        maxCounter = resultSet.getInt(1);
        resultSet.close();
    }

    /**
     * Метод закрывает коннект и очищает поля
     */
    public static void disconnect() {
        try {
            if (connection != null) {
                connection.close();
                connection = null;
            }
            Log.getInstance().getLogger(SqliteHandler.class).info("connection close \n");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Сооздаем экземпляр Statement
     * @return Statement
     * @throws SQLException
     */
    public static Statement getStatement() throws SQLException {
        return getConnection().createStatement();
    }

    /**
     * Сооздаем экземпляр PreparedStatement для добавления интервала
     * @return PreparedStatement
     * @throws SQLException
     */
    private static PreparedStatement createPrepareStatementForAddInterval() throws SQLException {

        return getConnection().prepareStatement(
                "INSERT INTO intervals (counter, dateCreate) VALUES (?, '" +  new java.sql.Date(new Date().getTime()) +"');");
    }

    /**
     * Добовляем интервал в БД
     * @throws SQLException
     */
    public static void incrementCounter() throws SQLException {
        PreparedStatement preparedStatement = createPrepareStatementForAddInterval();
        preparedStatement.setString(1, String.valueOf(++maxCounter));
        preparedStatement.executeUpdate();
        Log.getInstance().getLogger(Main.class).info("interval " + maxCounter + " added");
    }

}
