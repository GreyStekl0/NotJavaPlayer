package ui

import model.Track
import service.IPlayerService
import service.PlayerListener

class PlaybackManager(
    private val playerService: IPlayerService,
    private val playerListener: PlayerListener,
) {
    private var currentPlaylist: List<Track> = emptyList()
    private var trackIndex = -1

    fun playPlaylist(playlist: List<Track>) {
        if (playlist.isNotEmpty()) {
            currentPlaylist = playlist
            trackIndex = 0
            playCurrentTrack()
        }
    }

    fun playCurrentTrack() {
        if (trackIndex in currentPlaylist.indices) {
            val track = currentPlaylist[trackIndex]
            playerService.playTrack(track, playerListener)
        }
    }

    fun playPreviousTrack() {
        if (currentPlaylist.isNotEmpty()) {
            trackIndex = (trackIndex - 1 + currentPlaylist.size) % currentPlaylist.size
            playCurrentTrack()
        }
    }

    fun playNextTrack() {
        if (currentPlaylist.isNotEmpty()) {
            trackIndex = (trackIndex + 1) % currentPlaylist.size
            playCurrentTrack()
        }
    }

    fun replayCurrentTrack() {
        playCurrentTrack()
    }
}
