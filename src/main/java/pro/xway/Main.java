package pro.xway;

import java.sql.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {

    public static final String ORANGE = "orange";

    public static void main(String[] args) {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(() -> addProductToBd(ORANGE),
                0, 2, TimeUnit.SECONDS);
    }

    public static void addProductToBd(String productName) {
        try {
            SqliteHandler.addProduct(productName);
        } catch (SQLException e) {
            Log.getInstance().getLogger(Main.class).info(e.getMessage());
        } finally {
            SqliteHandler.disconnect();
        }

    }

}
