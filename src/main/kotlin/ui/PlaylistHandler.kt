package ui

import model.Playlist
import model.Track
import service.PlaylistService
import java.io.File

class PlaylistHandler(
    private val playlistService: PlaylistService,
    private val musicDirectory: File,
) {
    fun showAllSongs() {
        val allSongs = playlistService.getPlaylist(musicDirectory, "All songs")
        allSongs?.tracks?.forEach { println("${it.artist} - ${it.title}") }
    }

    fun createPlaylist(name: String) {
        playlistService.createPlaylist(musicDirectory, name)
    }

    fun removePlaylist(name: String) {
        playlistService.removePlaylist(musicDirectory, name)
    }

    fun showAllPlaylists() {
        val playlists = playlistService.getAllPlaylists(musicDirectory)
        playlists.forEach { println(it) }
    }

    fun showPlaylist(name: String): Playlist? {
        val playlist = playlistService.getPlaylist(musicDirectory, name)
        playlist?.tracks?.forEachIndexed { index, track ->
            println("$index: ${track.artist} - ${track.title}")
        }
        return playlist
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
