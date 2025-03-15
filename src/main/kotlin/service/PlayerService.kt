package service

import javazoom.jlgui.basicplayer.BasicPlayer
import javazoom.jlgui.basicplayer.BasicPlayerException
import model.Track
import org.slf4j.LoggerFactory
import java.io.File

class PlayerService {
    private val logger = LoggerFactory.getLogger(PlayerService::class.java)
    private var basicPlayer: BasicPlayer? = null
    private var isPlaying = false
    private var isPaused = false

    fun playTrack(track: Track) {
        stopCurrentTrack()

        basicPlayer = BasicPlayer()
        try {
            basicPlayer?.open(File(track.path))
            basicPlayer?.play()
            isPlaying = true
            isPaused = false
        } catch (ex: BasicPlayerException) {
            logger.error("Ошибка при воспроизведении трека: ${track.title}", ex)
        }
    }

    fun stopCurrentTrack() {
        try {
            basicPlayer?.stop()
        } catch (ex: BasicPlayerException) {
            logger.error("Ошибка при остановке воспроизведения", ex)
        }
        isPlaying = false
        isPaused = false
    }

    fun pauseAndResumeTrack() {
        if (!isPlaying) return

        if (isPaused) {
            try {
                basicPlayer?.resume()
                isPaused = false
            } catch (ex: BasicPlayerException) {
                logger.error("Ошибка при возобновлении воспроизведения", ex)
            }
        } else {
            try {
                basicPlayer?.pause()
                isPaused = true
            } catch (ex: BasicPlayerException) {
                logger.error("Ошибка при приостановке воспроизведения", ex)
            }
        }
    }
}
