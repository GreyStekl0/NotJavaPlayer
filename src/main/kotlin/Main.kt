import javazoom.jl.player.Player
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.jaudiotagger.audio.AudioFileIO
import org.jaudiotagger.tag.FieldKey
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream

fun playMp3(filePath: String) {
    val fis = FileInputStream(filePath)
    val bis = BufferedInputStream(fis)
    val player = Player(bis)
    player.play()
}

fun mp3Metadata(song: File): List<String> {
    val audioFile = AudioFileIO.read(song)
    val tag = audioFile.tag
    val title = tag.getFirst(FieldKey.TITLE)
    val artist = tag.getFirst(FieldKey.ARTIST)
    return listOf(title, artist)
}

@Serializable
data class Track(
    val path: String,
    val title: String,
    val artist: String,
)

@Serializable
data class Playlist(
    val name: String,
    val tracks: MutableList<Track> = mutableListOf(),
)

fun start(): File {
    println(
        """
        Здравствуй дорогой пользователь
        Для работы "NOTJavaPlayer" введите путь к папке с музыкой
        Или нажмите Enter для использования папки по умолчанию: 
        """.trimIndent(),
    )
    val directory = readln()
    if (directory.replace(" ", "") == "") {
        val homeDir = System.getProperty("user.home")
        return File(homeDir, "Music")
    } else {
        return File(directory)
    }
}

fun playerFolder(directory: File) {
    val jsonFolder = File(directory, ".NotJavaPlayer")
    if (!jsonFolder.exists()) {
        jsonFolder.mkdir()
    }
}

fun allSongs(directory: File): Playlist {
    val allSongsPlaylist = Playlist("All songs")
    val files = directory.listFiles() ?: return allSongsPlaylist

    for (file in files) {
        if (file.isDirectory) {
            if (!file.name.startsWith(".")) {
                val subDirPlaylist = allSongs(file)
                allSongsPlaylist.tracks.addAll(subDirPlaylist.tracks)
            }
        } else if (file.name.endsWith(".mp3")) {
            try {
                val mp3Data = mp3Metadata(file)
                val track = Track(file.path, mp3Data[0], mp3Data[1])
                allSongsPlaylist.tracks.add(track)
            } catch (e: Exception) {
                println("Error processing file ${file.path}: ${e.message}")
            }
        }
    }

    return allSongsPlaylist
}

fun createJsonFile(
    directory: File,
    playlist: Playlist,
) {
    val playerDir = File(directory, ".NotJavaPlayer")
    val jsonFile = File(playerDir, "${playlist.name}.json")
    val result = Json.encodeToString(playlist)
    jsonFile.writeText(result)
}

fun allSoundList(directory: File) {
    val playerDir = File(directory, ".NotJavaPlayer")
    val allSoundsPlaylist = File(playerDir, "All songs.json").readText()
    val allSounds = Json.decodeFromString<Playlist>(allSoundsPlaylist)
    for (track in allSounds.tracks) {
        println("${track.title} - ${track.artist}")
    }
}

fun main() {
    val directory = start()
    playerFolder(directory)
    createJsonFile(directory, allSongs(directory))
    while (true) {
        println("\nВыберите действие:")
        println("1) Показать список песен")
        println("2) Создать плейлист")
        println("3) Удалить плейлист")
        println("4) Показать все плейлисты")
        println("5) Вывести плейлист")
        println("6) Включить плейлист")
        println("7) Добавить песню в плейлист")
        println("8) Убрать песню из плейлиста")
        println("9) Включить предыдущую песню")
        println("10) Включить следующую песню")
        println("11) Повторить текущую песню")
        println("0) Выйти\n")

        val choice = readln().toInt()
        when (choice) {
            1 -> {
                allSoundList(directory)
            }
            2 -> {
                println("Введите название плейлиста:")
                val playlistName = readln()
                val playlist = Playlist(playlistName)
                createJsonFile(directory, playlist)
            }

            0 -> {
                break
            }

            else -> {
                println("Неверный выбор\n")
            }
        }
    }
}
