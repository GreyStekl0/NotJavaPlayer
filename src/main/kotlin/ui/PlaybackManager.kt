package ui

import model.Track
import service.PlayerService

class PlaybackManager(
    private val playerService: PlayerService,
) {
    private var currentPlaylist: List<Track> = emptyList()
    private var currentTrackIndex = -1

    fun playPlaylist(playlist: List<Track>) {
        if (playlist.isNotEmpty()) {
            currentPlaylist = playlist
            currentTrackIndex = 0
            playCurrentTrack()
        }
    }

    fun playCurrentTrack() {
        if (currentTrackIndex >= 0 && currentTrackIndex < currentPlaylist.size) {
            val track = currentPlaylist[currentTrackIndex]
            playerService.playTrack(track)
        }
    }

    fun playPreviousTrack() {
        if (currentPlaylist.isNotEmpty()) {
            currentTrackIndex = (currentTrackIndex - 1 + currentPlaylist.size) % currentPlaylist.size
            playCurrentTrack()
        }
    }

    fun playNextTrack() {
        if (currentPlaylist.isNotEmpty()) {
            currentTrackIndex = (currentTrackIndex + 1) % currentPlaylist.size
            playCurrentTrack()
        }
    }

    fun replayCurrentTrack() {
        playCurrentTrack()
    }
}
