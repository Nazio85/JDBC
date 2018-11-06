package pro.xway;

import java.sql.*;

public class Main {

    public static void main(String[] args) {
        try {
            Statement statement = SqliteHandler.getStatement();

            statement.executeUpdate("insert into students (name) values ('Olia')");
            ResultSet rs = statement.executeQuery("select * from students;");
            while (rs.next()){
                System.out.println(rs.getInt(1) + " " + rs.getString(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SqliteHandler.disconnect();
        }

    }

}
