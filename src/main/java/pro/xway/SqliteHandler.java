package pro.xway;

import java.sql.*;
import java.util.List;

public class SqliteHandler {
    private static Connection connection;
    private static PreparedStatement preparedStatementAddProduct;
    private static Statement statement;

    private SqliteHandler() {
    }

    public static Connection getConnection() throws SQLException {
        if (connection == null) {
            connection = DriverManager.getConnection("jdbc:sqlite:main.db");
            Statement statement = SqliteHandler.connection.createStatement();
            statement.executeUpdate(
                    "CREATE TABLE if not exists products (" +
                            " id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                            " name TEXT NOT NULL );");
            statement.close();
        }
        Log.getInstance().getLogger(SqliteHandler.class).info("connection open");

        return connection;
    }

    public static void disconnect() {
        try {
            if (statement != null) {
                statement.close();
                statement = null;
            }
            if (preparedStatementAddProduct != null) {
                preparedStatementAddProduct.close();
                preparedStatementAddProduct = null;
            }
            if (connection != null) {
                connection.close();
                connection = null;
            }
            Log.getInstance().getLogger(SqliteHandler.class).info("connection close \n");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Statement getStatement() throws SQLException {
        if (statement == null)
            statement = getConnection().createStatement();
        return statement;
    }

    private static PreparedStatement createPrepareStatementForAddProduct() throws SQLException {
        return preparedStatementAddProduct = getConnection().prepareStatement(
                "INSERT INTO products (name) VALUES (?);");
    }

    public static void addProduct(String productName) throws SQLException {
        PreparedStatement preparedStatement = createPrepareStatementForAddProduct();
        preparedStatement.setString(1, productName);
        preparedStatement.executeUpdate();
        Log.getInstance().getLogger(Main.class).info("product " + productName + " added ");
    }

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
