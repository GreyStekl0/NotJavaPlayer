package repository

import model.Playlist
import service.FileService
import service.MetadataService
import java.io.File

class PlaylistRepository(
    private val fileService: FileService,
    private val metadataService: MetadataService,
) : IPlaylistRepository {
    override fun getAllSongs(directory: File): Playlist {
        val allSongs = Playlist("All songs")
        val mp3Files = fileService.findAllMp3Files(directory)
        for (file in mp3Files) {
            val track = metadataService.createTrack(file)
            allSongs.tracks.add(track)
        }
        return allSongs
    }

    override fun getAllPlaylists(directory: File): List<String> {
        val playerDir = fileService.createPlayerFolder(directory)
        return playerDir
            .listFiles()
            ?.filter { it.isFile && it.extension == "json" }
            ?.map { it.nameWithoutExtension }
            ?: emptyList()
    }

    override fun getPlaylist(
        directory: File,
        name: String,
    ): Playlist? {
        val playerDir = fileService.createPlayerFolder(directory)
        val playlistFile = File(playerDir, "$name.json")
        return if (playlistFile.exists()) {
            fileService.loadPlaylistFromJson(playlistFile)
        } else {
            null
        }
    }

    override fun savePlaylist(
        directory: File,
        playlist: Playlist,
    ) {
        fileService.savePlaylistToJson(directory, playlist)
    }

    override fun deletePlaylist(
        directory: File,
        name: String,
    ) {
        val playerDir = fileService.createPlayerFolder(directory)
        val playlistFile = File(playerDir, "$name.json")
        if (playlistFile.exists()) {
            playlistFile.delete()
        }
    }
}
