package service

import model.Playlist
import java.io.File

interface IFileService {
    fun createPlayerFolder(directory: File): File

    fun findAllMp3Files(directory: File): List<File>

    fun getDefaultDirectory(): File

    fun savePlaylistToJson(
        directory: File,
        playlist: Playlist,
    )

    fun loadPlaylistFromJson(file: File): Playlist
}
