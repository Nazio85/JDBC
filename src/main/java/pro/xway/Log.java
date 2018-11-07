package pro.xway;


import java.io.IOException;
import java.time.LocalDate;
import java.util.logging.*;

/**
 * Класс создает логер и пишет копию в файл.
 * @author Хайрутдинов Александр
 *
 */
public class Log {
    private static Log ourInstance;
    private Handler fileHandler;

    public static Log getInstance() {
        if (ourInstance == null) ourInstance = new Log();

        return ourInstance;
    }

    private Log() {
    }

    public Logger getLogger(Class cl) {
        Logger logger = Logger.getLogger(cl.getName());
        createFileHandler(logger);
        return logger;
    }

    /**
     * Добавляет Logger'у FileHandler
     * @param logger
     */
    private void createFileHandler(Logger logger) {
        try {
            if (fileHandler == null) {
                fileHandler = new FileHandler(LocalDate.now() + ".log");
                fileHandler.setLevel(Level.ALL);
                fileHandler.setFormatter(new SimpleFormatter());
            }

            logger.addHandler(fileHandler);
        } catch (IOException e) {
            logger.info(e.getMessage());
        }
    }
}
