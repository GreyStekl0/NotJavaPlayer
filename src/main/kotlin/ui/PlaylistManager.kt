package ui

import model.Playlist
import model.Track
import service.IPlaylistService
import java.io.File

class PlaylistManager(
    private val playlistService: IPlaylistService,
    private val musicDirectory: File,
) {
    fun createPlaylist(name: String) {
        playlistService.createPlaylist(musicDirectory, name)
    }

    fun removePlaylist(name: String) {
        playlistService.removePlaylist(musicDirectory, name)
    }

    fun getTrack(
        playlist: Playlist,
        index: Int?,
        title: String,
    ): Track? =
        if (index != null && index > 0 && index < playlist.tracks.size) {
            playlist.tracks[index - 1]
        } else {
            playlist.tracks.find { it.title == title }
        }

    fun addTrackToPlaylist(
        playlistName: String,
        track: Track,
    ) {
        playlistService.addTrackToPlaylist(musicDirectory, playlistName, track)
    }

    fun removeTrackFromPlaylist(
        playlistName: String,
        track: Track,
    ) {
        playlistService.removeTrackFromPlaylist(musicDirectory, playlistName, track)
    }

    fun getPlaylist(name: String): Playlist? = playlistService.getPlaylist(musicDirectory, name)
}
