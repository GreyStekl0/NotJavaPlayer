package ui

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import model.Playlist
import model.Track
import service.FileService
import service.PlayerService
import service.PlaylistService
import java.io.File

class ConsoleUI(
    private val fileService: FileService,
    private val playlistService: PlaylistService,
    private val playerService: PlayerService,
) {
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private lateinit var musicDirectory: File
    private var currentPlaylist: List<Track> = emptyList()
    private var currentTrackIndex = -1

    fun start() {
        musicDirectory = requestMusicDirectory()
        fileService.createPlayerFolder(musicDirectory)

        val allSongs = playlistService.getAllSongs(musicDirectory)
        playlistService.savePlaylist(musicDirectory, allSongs)

        mainLoop()
    }

    private fun requestMusicDirectory(): File {
        println(
            """
            Здравствуй дорогой пользователь
            Для работы "NOTJavaPlayer" введите путь к папке с музыкой
            Или нажмите Enter для использования папки по умолчанию:
            """.trimIndent(),
        )
        val directoryInput = readln()
        return if (directoryInput.replace(" ", "") == "") {
            fileService.getDefaultDirectory()
        } else {
            File(directoryInput)
        }
    }

    private fun mainLoop() {
        while (true) {
            displayMenu()
            val choice = readln().toIntOrNull() ?: continue

            when (choice) {
                1 -> showAllSongs()
                2 -> createPlaylist()
                3 -> removePlaylist()
                4 -> showAllPlaylists()
                5 -> showPlaylist()
                6 -> playPlaylist()
                7 -> addSongToPlaylist()
                8 -> removeSongFromPlaylist()
                9 -> playPreviousTrack()
                10 -> playNextTrack()
                11 -> replayCurrentTrack()
                12 -> playerService.pauseAndResumeTrack()
                0 -> break
                else -> println("Неверный выбор\n")
            }
        }
    }

    private fun displayMenu() {
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
        println("12) Поставить на паузу/возобновить воспроизведение")
        println("0) Выйти\n")
    }

    // Реализации методов для обработки выбора пользователя
    private fun showAllSongs() {
        val allSongs = playlistService.getPlaylist(musicDirectory, "All songs")
        allSongs?.tracks?.forEach { println("${it.artist} - ${it.title}") }
    }

    private fun createPlaylist() {
        println("Введите название плейлиста:")
        val name = readln()
        playlistService.createPlaylist(musicDirectory, name)
        println("Плейлист '$name' создан")
    }

    private fun removePlaylist() {
        println("Введите название плейлиста:")
        val name = readln()
        playlistService.removePlaylist(musicDirectory, name)
        println("Плейлист удален")
    }

    private fun showAllPlaylists() {
        val playlists = playlistService.getAllPlaylists(musicDirectory)
        playlists.forEach { println(it) }
    }

    private fun showPlaylist() {
        println("Введите название плейлиста:")
        val name = readln()
        val playlist = playlistService.getPlaylist(musicDirectory, name)
        playlist?.tracks?.forEachIndexed { index, track ->
            println("$index: ${track.artist} - ${track.title}")
        } ?: println("Плейлист не найден")
    }

    private fun playPlaylist() {
        println("Введите название плейлиста:")
        val name = readln()
        val playlist = playlistService.getPlaylist(musicDirectory, name)

        if (playlist != null && playlist.tracks.isNotEmpty()) {
            currentPlaylist = playlist.tracks
            currentTrackIndex = 0
            playCurrentTrack()
        } else {
            println("Плейлист не найден или пуст")
        }
    }

    private fun playCurrentTrack() {
        if (currentTrackIndex >= 0 && currentTrackIndex < currentPlaylist.size) {
            val track = currentPlaylist[currentTrackIndex]
            println("Воспроизведение: ${track.artist} - ${track.title}")

            coroutineScope.launch {
                playerService.playTrack(track)
            }
        }
    }

    private fun getTrack(
        playlist: Playlist,
        index: Int?,
        title: String,
    ): Track? =
        if (index != null && index >= 0 && index < playlist.tracks.size) {
            playlist.tracks[index - 1]
        } else {
            playlist.tracks.find { it.title == title }
        }

    private fun addSongToPlaylist() {
        println("Введите название плейлиста:")
        val playlistName = readln()
        val playlist = playlistService.getPlaylist(musicDirectory, playlistName)
        println("Введите номер песни или название песни, которую хотите добавить: ")
        val trackInput = readln()
        val track = getTrack(playlist!!, trackInput.toIntOrNull(), trackInput)
        if (track != null) {
            playlistService.addTrackToPlaylist(musicDirectory, playlistName, track)
            println("Песня добавлена в плейлист")
        } else {
            println("Песня не найдена")
        }
    }

    private fun removeSongFromPlaylist() {
        println("Введите название плейлиста:")
        val playlistName = readln()
        val playlist = playlistService.getPlaylist(musicDirectory, playlistName)
        println("Введите номер песни или название песни, которую хотите удалить: ")
        val trackInput = readln()
        val track = getTrack(playlist!!, trackInput.toIntOrNull(), trackInput)
        if (track != null) {
            playlistService.removeTrackFromPlaylist(musicDirectory, playlistName, track)
            println("Песня удалена из плейлиста")
        } else {
            println("Песня не найдена")
        }
    }

    private fun playPreviousTrack() {
        if (currentPlaylist.isNotEmpty()) {
            currentTrackIndex = (currentTrackIndex - 1 + currentPlaylist.size) % currentPlaylist.size
            playCurrentTrack()
        }
    }

    private fun playNextTrack() {
        if (currentPlaylist.isNotEmpty()) {
            currentTrackIndex = (currentTrackIndex + 1) % currentPlaylist.size
            playCurrentTrack()
        }
    }

    private fun replayCurrentTrack() {
        playCurrentTrack()
    }
}
