package config

import java.util.logging.Level
import java.util.logging.Logger

object LoggerConfig {
    fun configure() {
        Logger.getLogger("org.jaudiotagger").setLevel(Level.OFF)
    }
}
