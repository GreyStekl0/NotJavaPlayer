package ui

import model.Playlist
import service.IPlaylistService
import java.io.File

class PlaylistDisplay(
    private val playlistService: IPlaylistService,
    private val musicDirectory: File,
) {
    fun showAllSongs() {
        val allSongs = playlistService.getPlaylist(musicDirectory, "All songs")
        if (allSongs?.tracks?.isEmpty() == true) {
            println("Песен нет")
            return
        } else {
            allSongs?.tracks?.forEachIndexed { index, track ->
                println("${index + 1}) ${track.artist} - ${track.title}")
            }
        }
    }

    fun showAllPlaylists() {
        val playlists = playlistService.getAllPlaylists(musicDirectory)
        playlists.forEach { println(it) }
    }

    fun showPlaylist(name: String): Playlist? {
        val playlist = playlistService.getPlaylist(musicDirectory, name)
        if (playlist != null) {
            if (playlist.tracks.isEmpty()) {
                println("В плейлисте нет песен")
            } else {
                playlist.tracks.forEachIndexed { index, track ->
                    println("${index + 1}) ${track.artist} - ${track.title}")
                }
            }
            return playlist
        } else {
            println("Плейлист '$name' не найден")
            return null
        }
    }
}
