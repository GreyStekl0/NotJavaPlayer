package service

import javazoom.jlgui.basicplayer.BasicPlayerListener
import model.Track

interface IPlayerService {
    fun isPlaying(): Boolean

    fun isPaused(): Boolean

    fun playTrack(
        track: Track,
        listener: BasicPlayerListener,
    )

    fun stopCurrentTrack()

    fun pauseAndResumeTrack()
}
