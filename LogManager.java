import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LogManager {
    private static Logger logger = Logger.getLogger("InventoryLog");

    public static void setup() {
        try {
            // Create a FileHandler to write log messages to a file
            FileHandler fileHandler = new FileHandler("inventory.log", true);

            // Set the formatter to a simple text format
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);

            // Add the handler to the logger
            logger.addHandler(fileHandler);

            // Set the logger level
            logger.setUseParentHandlers(false); // Disable console logging if needed

        } catch (IOException e) {
            logger.severe("Failed to set up logger: " + e.getMessage());
        }
    }

    public static Logger getLogger() {
        return logger;
    }
}
