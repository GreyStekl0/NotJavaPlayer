package service

import javazoom.jlgui.basicplayer.BasicPlayer
import javazoom.jlgui.basicplayer.BasicPlayerException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import model.Track
import java.io.File

class PlayerService {
    private var basicPlayer: BasicPlayer? = null
    private var isPlaying = false
    private var isPaused = false

    // Запускает воспроизведение трека
    suspend fun playTrack(track: Track) =
        withContext(Dispatchers.IO) {
            stopCurrentTrack() // остановка предыдущего трека, если он играет

            basicPlayer = BasicPlayer() // создаем новый плеер
            try {
                basicPlayer?.open(File(track.path))
                basicPlayer?.play() // запуск воспроизведения
                isPlaying = true
                isPaused = false
            } catch (ex: BasicPlayerException) {
                ex.printStackTrace()
            }
        }

    // Останавливает воспроизведение (если необходимо)
    fun stopCurrentTrack() {
        try {
            basicPlayer?.stop()
        } catch (ex: BasicPlayerException) {
            ex.printStackTrace()
        }
        isPlaying = false
        isPaused = false
    }

    // Приостанавливает воспроизведение
    fun pauseAndResumeTrack() {
        if (isPlaying && !isPaused) {
            try {
                basicPlayer?.pause()
                isPaused = true
            } catch (ex: BasicPlayerException) {
                ex.printStackTrace()
            }
        } else if (isPlaying && isPaused) {
            try {
                basicPlayer?.resume()
                isPaused = false
            } catch (ex: BasicPlayerException) {
                ex.printStackTrace()
            }
        }
    }
}
