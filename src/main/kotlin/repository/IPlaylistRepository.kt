package repository

import model.Playlist
import java.io.File

interface IPlaylistRepository {
    fun getAllSongs(directory: File): Playlist

    fun getAllPlaylists(directory: File): List<String>

    fun getPlaylist(
        directory: File,
        name: String,
    ): Playlist?

    fun savePlaylist(
        directory: File,
        playlist: Playlist,
    )

    fun deletePlaylist(
        directory: File,
        name: String,
    ): Boolean
}
