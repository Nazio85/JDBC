package pro.xway;


import java.io.IOException;
import java.time.LocalDate;
import java.util.logging.*;

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

    private void createFileHandler(Logger logger) {
        try {
            if (fileHandler == null) {
                fileHandler = new FileHandler("log" + LocalDate.now() + ".txt");
                fileHandler.setLevel(Level.ALL);
                fileHandler.setFormatter(new SimpleFormatter());
            }

            logger.addHandler(fileHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
