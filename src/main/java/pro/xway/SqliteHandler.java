package pro.xway;

import java.sql.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * Класс управляет подключением к базе данных и создает шаблоны для PreparedStatement
 * @author Хайрутдинов Александр
 *
*/
public class SqliteHandler {
    private static Connection connection;

    private SqliteHandler() {
    }

    /**
     * Метод создает Connection
     * @return возвращает Connection
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null) {
            connection = DriverManager.getConnection("jdbc:sqlite:main.db");
            Statement statement = SqliteHandler.connection.createStatement();
            statement.executeUpdate(
                    "CREATE TABLE if not exists products (" +
                            " id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                            " name TEXT NOT NULL," +
                            " dateCreate DATE NOT NULL );");
            statement.close();
        }
        Log.getInstance().getLogger(SqliteHandler.class).info("connection open");

        return connection;
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
     * Сооздаем экземпляр PreparedStatement для добавления продукта
     * @return PreparedStatement
     * @throws SQLException
     */
    private static PreparedStatement createPrepareStatementForAddProduct() throws SQLException {

        return getConnection().prepareStatement(
                "INSERT INTO products (name, dateCreate) VALUES (?, '" +  new java.sql.Date(new Date().getTime()) +"');");
    }

    /**
     * Добовляем продукт в БД
     * @param productName
     * @throws SQLException
     */
    public static void addProduct(String productName) throws SQLException {
        PreparedStatement preparedStatement = createPrepareStatementForAddProduct();
        preparedStatement.setString(1, productName);
        preparedStatement.executeUpdate();
        Log.getInstance().getLogger(Main.class).info("product " + productName + " added ");
    }

    /**
     * Добовляем лист продуктов в БД
     * @param products
     * @throws SQLException
     */
    public static void addProducts(List<String> products) throws SQLException {
        PreparedStatement preparedStatement = createPrepareStatementForAddProduct();
        connection.setAutoCommit(false);
        for (String productName : products) {
            preparedStatement.setString(1, productName);
            preparedStatement.addBatch();
        }
        preparedStatement.executeBatch();
        connection.setAutoCommit(true);
    }
}
