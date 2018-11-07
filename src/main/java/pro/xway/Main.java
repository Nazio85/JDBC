package pro.xway;

import java.sql.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
* Класс добавляет интервал в базу данных по расписанию
* @author Хайрутдинов Александр
*
*
 */
public class Main {

    public static int INTERVAL_NUMBER = 0;

    public static void main(String[] args) {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(() -> addIntervalToBd(++INTERVAL_NUMBER),
                0, 1, TimeUnit.HOURS);
    }

    public static void addIntervalToBd(int intervalNumber) {
        try {
            SqliteHandler.incrementCounter(intervalNumber);
        } catch (SQLException e) {
            Log.getInstance().getLogger(Main.class).info(e.getMessage());
        } finally {
            SqliteHandler.disconnect();
        }
    }

}
