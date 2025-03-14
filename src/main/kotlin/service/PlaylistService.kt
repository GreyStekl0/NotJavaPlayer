package service

import model.Playlist
import model.Track
import repository.PlaylistRepository
import java.io.File

class PlaylistService(
    private val repository: PlaylistRepository,
) {
    fun getAllSongs(directory: File): Playlist = repository.getAllSongs(directory)

    fun savePlaylist(
        directory: File,
        playlist: Playlist,
    ) {
        repository.savePlaylist(directory, playlist)
    }

    fun createPlaylist(
        directory: File,
        name: String,
    ) {
        val playlist = Playlist(name)
        repository.savePlaylist(directory, playlist)
    }

    fun removePlaylist(
        directory: File,
        name: String,
    ) {
        repository.deletePlaylist(directory, name)
    }

    fun getAllPlaylists(directory: File): List<String> = repository.getAllPlaylists(directory)

    fun getPlaylist(
        directory: File,
        name: String,
    ): Playlist? = repository.getPlaylist(directory, name)

    fun addTrackToPlaylist(
        directory: File,
        playlistName: String,
        track: Track,
    ): Boolean {
        val playlist = getPlaylist(directory, playlistName) ?: return false
        playlist.tracks.add(track)
        repository.savePlaylist(directory, playlist)
        return true
    }

    fun removeTrackFromPlaylist(
        directory: File,
        playlistName: String,
        track: Track,
    ): Boolean {
        val playlist = getPlaylist(directory, playlistName)
        if (playlist == null || track !in playlist.tracks) {
            return false
        }
        playlist.tracks.remove(track)
        repository.savePlaylist(directory, playlist)
        return true
    }
}
