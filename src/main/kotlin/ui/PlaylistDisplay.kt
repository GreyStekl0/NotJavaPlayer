package ui

import model.Playlist
import service.PlaylistService
import java.io.File

class PlaylistDisplay(
    private val playlistService: PlaylistService,
    private val musicDirectory: File,
) {
    fun showAllSongs() {
        val allSongs = playlistService.getPlaylist(musicDirectory, "All songs")
        allSongs?.tracks?.forEach { println("${it.artist} - ${it.title}") }
    }

    fun showAllPlaylists() {
        val playlists = playlistService.getAllPlaylists(musicDirectory)
        playlists.forEach { println(it) }
    }

    fun showPlaylist(name: String): Playlist? {
        val playlist = playlistService.getPlaylist(musicDirectory, name)
        playlist?.tracks?.forEachIndexed { index, track ->
            println("${index + 1}: ${track.artist} - ${track.title}")
        }
        return playlist
    }
}
