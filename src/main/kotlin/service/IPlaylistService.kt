package service

import model.Playlist
import model.Track
import java.io.File

interface IPlaylistService {
    fun getAllSongs(directory: File): Playlist

    fun savePlaylist(
        directory: File,
        playlist: Playlist,
    )

    fun createPlaylist(
        directory: File,
        name: String,
    )

    fun removePlaylist(
        directory: File,
        name: String,
    )

    fun getAllPlaylists(directory: File): List<String>

    fun getPlaylist(
        directory: File,
        name: String,
    ): Playlist?

    fun addTrackToPlaylist(
        directory: File,
        playlistName: String,
        track: Track,
    ): Boolean

    fun removeTrackFromPlaylist(
        directory: File,
        playlistName: String,
        track: Track,
    ): Boolean
}
