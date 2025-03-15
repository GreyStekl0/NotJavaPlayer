package service

import kotlinx.serialization.json.Json
import model.Playlist
import java.io.File

class FileService : IFileService {
    override fun createPlayerFolder(directory: File): File {
        val jsonFolder = File(directory, ".NotJavaPlayer")
        if (!jsonFolder.exists()) {
            jsonFolder.mkdir()
        }
        return jsonFolder
    }

    override fun findAllMp3Files(directory: File): List<File> {
        val result = mutableListOf<File>()
        val files = directory.listFiles() ?: return result

        for (file in files) {
            if (file.isDirectory) {
                if (!file.name.startsWith(".")) {
                    result.addAll(findAllMp3Files(file))
                }
            } else if (file.name.endsWith(".mp3")) {
                result.add(file)
            }
        }
        return result
    }

    override fun getDefaultDirectory(): File {
        val home = System.getProperty("user.home")
        return File(home)
    }

    override fun savePlaylistToJson(
        directory: File,
        playlist: Playlist,
    ) {
        val playerDir = createPlayerFolder(directory)
        val jsonFile = File(playerDir, "${playlist.name}.json")
        val result = Json.encodeToString(Playlist.serializer(), playlist)
        jsonFile.writeText(result)
    }

    override fun loadPlaylistFromJson(file: File): Playlist {
        val data = file.readText()
        return Json.decodeFromString(Playlist.serializer(), data)
    }
}
