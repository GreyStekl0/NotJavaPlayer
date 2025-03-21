package config

import java.util.logging.Level
import java.util.logging.Logger

object LoggerConfig {
    fun configure() {
        // Настройка java.util.logging
        Logger.getLogger("org.jaudiotagger").setLevel(Level.OFF)

        // Настройка SLF4J Simple Logger
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "ERROR")
    }
}
