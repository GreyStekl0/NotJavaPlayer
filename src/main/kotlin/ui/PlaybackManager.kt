package ui

import javazoom.jlgui.basicplayer.BasicPlayerListener
import model.Track
import service.IPlayerService

class PlaybackManager(
    private val playerService: IPlayerService,
    private val playerListener: BasicPlayerListener,
) {
    private var currentPlaylist: List<Track> = emptyList()
    private var trackIndex = -1
    private val playbackDisplay = PlaybackDisplay()

    fun playPlaylist(playlist: List<Track>) {
        if (playlist.isNotEmpty()) {
            currentPlaylist = playlist
            trackIndex = 0
            playCurrentTrack()
        }
    }

    fun playCurrentTrack(): Boolean =
        if (trackIndex in currentPlaylist.indices) {
            val track = currentPlaylist[trackIndex]
            playerService.playTrack(track, playerListener)
            playbackDisplay.showNowPlaying(track.artist, track.title)
            true
        } else {
            false
        }

    fun playPreviousTrack(): Boolean =
        if (currentPlaylist.isNotEmpty()) {
            trackIndex = (trackIndex - 1 + currentPlaylist.size) % currentPlaylist.size
            playCurrentTrack()
            true
        } else {
            false
        }

    fun playNextTrack(): Boolean =
        if (currentPlaylist.isNotEmpty()) {
            trackIndex = (trackIndex + 1) % currentPlaylist.size
            playCurrentTrack()
            true
        } else {
            false
        }

    fun replayCurrentTrack(): Boolean = playCurrentTrack()
}
