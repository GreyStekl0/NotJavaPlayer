package service

import kotlinx.serialization.json.Json
import model.Playlist
import java.io.File

class JsonService : IJsonService {
    override fun savePlaylistToJson(
        directory: File,
        playlist: Playlist,
    ) {
        val playerDir = File(directory, ".NotJavaPlayer")
        if (!playerDir.exists()) {
            playerDir.mkdir()
        }
        val jsonFile = File(playerDir, "${playlist.name}.json")
        val result = Json.encodeToString(Playlist.serializer(), playlist)
        jsonFile.writeText(result)
    }

    override fun loadPlaylistFromJson(file: File): Playlist {
        val data = file.readText()
        return Json.decodeFromString(Playlist.serializer(), data)
    }
}
