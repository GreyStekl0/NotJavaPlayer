package config

import java.util.logging.Level
import java.util.logging.Logger

object LoggerConfig {
    fun configure() {
        // Настройка java.util.logging
        Logger.getLogger("org.jaudiotagger").setLevel(Level.WARNING)

        // Настройка SLF4J Simple Logger
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "ERROR")
//        System.setProperty("org.slf4j.simpleLogger.log.javazoom.jlgui.basicplayer", "OFF")
    }
}
