package service

import model.Track

interface IPlayerService {
    fun isPlaying(): Boolean

    fun isPaused(): Boolean

    fun playTrack(
        track: Track,
        listener: PlayerListener,
    )

    fun stopCurrentTrack()

    fun pauseAndResumeTrack()
}
