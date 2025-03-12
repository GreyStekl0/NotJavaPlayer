import javazoom.jl.player.Player
import kotlinx.serialization.Serializable
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

fun printMp3Metadata(filePath: String) {
    try {
        val audioFile = AudioFileIO.read(File(filePath))
        val tag = audioFile.tag
        val title = tag.getFirst(FieldKey.TITLE)
        val artist = tag.getFirst(FieldKey.ARTIST)

        println("Title: $title")
        println("Artist: $artist")
    } catch (e: Exception) {
        e.printStackTrace()
    }
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

fun test() {
    val track1 = Track("/home/stekl0/Downloads/Anacondaz - Привет, Г..mp3", "Привет, Г", "anacondaz")
    val track2 = Track("/home/stekl0/Downloads/Anacondaz - Акуле плевать.mp3", "Акуле плевать", "anacondaz")
    val playlist = Playlist("тест")
    println(playlist)

    val newTrack = Track("/home/stekl0/Downloads/Anacondaz - Дождь.mp3", "Дождь", "anacondaz")
    playlist.tracks.add(newTrack)

    println(playlist)
    println(playlist.tracks[0].artist)
}

fun main() {
    test()
//    while (true) {
//        println("1) Добавить песню")
//        println("2) Показать список песен")
//        println("3) Удалить песню")
//        println("4) Создать плейлист")
//        println("5) Удалить плейлист")
//        println("6) Показать все плейлисты")
//        println("7) Вывести плейлист")
//        println("8) Включить плейлист")
//        println("9) Добавить песню в плейлист")
//        println("10) Убрать песню из плейлиста")
//        println("11) Включить предыдущую песню")
//        println("12) Включить следующую песню")
//        println("12) Повторить текущую песню")
//        println("0) Выйти\n")
//
//        val choice = readln().toInt()
//        when (choice) {
//            1 -> {
//                test()
//            }
//            0 -> {
//                break
//            }
//
//            else -> {
//                println("Неверный выбор\n")
//            }
//        }
//    }
}
