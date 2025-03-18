package service

import javazoom.jlgui.basicplayer.BasicPlayer
import javazoom.jlgui.basicplayer.BasicPlayerException
import javazoom.jlgui.basicplayer.BasicPlayerListener
import model.Track
import org.slf4j.LoggerFactory
import java.io.File

class PlayerService : IPlayerService {
    private val logger = LoggerFactory.getLogger(PlayerService::class.java)
    private var basicPlayer: BasicPlayer? = null
    private var isPlaying = false
    private var isPaused = false

    override fun isPlaying(): Boolean = isPlaying

    override fun isPaused(): Boolean = isPaused

    override fun playTrack(
        track: Track,
        listener: BasicPlayerListener,
    ) {
        stopCurrentTrack()

        basicPlayer = BasicPlayer()
        basicPlayer?.addBasicPlayerListener(listener)
        try {
            val file = File(track.path)
            if (!file.exists()) {
                println("Файл не найден, проверьте существует ли он по пути: ${track.path}")
                return
            }
            basicPlayer?.open(File(track.path))
            basicPlayer?.play()
            isPlaying = true
            isPaused = false
        } catch (ex: BasicPlayerException) {
            logger.error("Ошибка при воспроизведении трека: ${track.title}", ex)
        }
    }

    override fun stopCurrentTrack() {
        try {
            basicPlayer?.stop()
        } catch (ex: BasicPlayerException) {
            logger.error("Ошибка при остановке воспроизведения", ex)
        }
        isPlaying = false
        isPaused = false
    }

    override fun pauseAndResumeTrack() {
        if (!isPlaying) {
            println("Нет активного трека")
            return
        }

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
