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

    public static void main(String[] args) {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(Main::addIntervalToBd,
                0, 1, TimeUnit.SECONDS);
    }

    public static void addIntervalToBd() {
        try {
            SqliteHandler.incrementCounter();
        } catch (SQLException e) {
            Log.getInstance().getLogger(Main.class).info(e.getMessage());
        } finally {
            SqliteHandler.disconnect();
        }
    }

}
