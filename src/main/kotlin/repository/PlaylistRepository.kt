package repository

import model.Playlist
import service.IDirectoryService
import service.IJsonService
import service.IMetadataService
import java.io.File

class PlaylistRepository(
    private val directoryService: IDirectoryService,
    private val jsonService: IJsonService,
    private val metadataService: IMetadataService,
) : IPlaylistRepository {
    override fun getAllSongs(directory: File): Playlist {
        val allSongs = Playlist("All songs")
        val mp3Files = directoryService.findAllMp3Files(directory)
        for (file in mp3Files) {
            val track = metadataService.createTrack(file)
            allSongs.tracks.add(track)
        }
        return allSongs
    }

    override fun getAllPlaylists(directory: File): List<String> {
        val playerDir = directoryService.createPlayerFolder(directory)
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
        val playerDir = directoryService.createPlayerFolder(directory)
        val playlistFile = File(playerDir, "$name.json")
        return if (playlistFile.exists()) {
            jsonService.loadPlaylistFromJson(playlistFile)
        } else {
            null
        }
    }

    override fun savePlaylist(
        directory: File,
        playlist: Playlist,
    ) {
        jsonService.savePlaylistToJson(directory, playlist)
    }

    override fun deletePlaylist(
        directory: File,
        name: String,
    ): Boolean {
        val playerDir = directoryService.createPlayerFolder(directory)
        val playlistFile = File(playerDir, "$name.json")
        return if (playlistFile.exists()) {
            playlistFile.delete()
            true
        } else {
            false
        }
    }
}
