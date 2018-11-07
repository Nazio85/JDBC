package pro.xway;


import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;

public class SqliteHandlerTest {

    @Test
    public void checkConnection() {
        try {
            Assert.assertNotNull(SqliteHandler.getConnection());
        } catch (SQLException e) {
            Log.getInstance().getLogger(SqliteHandlerTest.class).info(e.getMessage());
        } finally {
            SqliteHandler.disconnect();
        }
    }

    @Test
    public void checkConnectionClose() {
        try {
            SqliteHandler.getConnection();
        } catch (SQLException e) {
            Log.getInstance().getLogger(SqliteHandlerTest.class).info(e.getMessage());
        } finally {
            SqliteHandler.disconnect();
            try {
                Class<SqliteHandler> sqliteHandlerClass = SqliteHandler.class;
                Field connection = sqliteHandlerClass.getDeclaredField("connection");
                connection.setAccessible(true);
                Assert.assertNull(connection.get(Connection.class));
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}