package service

import model.Playlist
import java.io.File

interface IJsonService {
    fun savePlaylistToJson(
        directory: File,
        playlist: Playlist,
    )

    fun loadPlaylistFromJson(file: File): Playlist
}
