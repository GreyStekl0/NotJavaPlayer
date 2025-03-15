package ui

import service.FileService
import service.PlayerService
import service.PlaylistService
import java.io.File

class ConsoleUI(
    private val fileService: FileService,
    private val playlistService: PlaylistService,
    private val playerService: PlayerService,
) {
    private lateinit var musicDirectory: File
    private lateinit var playbackManager: PlaybackManager
    private lateinit var playlistHandler: PlaylistHandler
    private lateinit var userInputHandler: UserInputHandler

    fun start() {
        musicDirectory = requestMusicDirectory()
        fileService.createPlayerFolder(musicDirectory)

        playbackManager = PlaybackManager(playerService)
        playlistHandler = PlaylistHandler(playlistService, musicDirectory)
        userInputHandler = UserInputHandler(playlistHandler, playbackManager)

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
        var running = true
        while (running) {
            displayMenu()
            running = processUserChoice()
        }
    }

    private fun processUserChoice(): Boolean {
        val input = readln()
        val choice = input.toIntOrNull()

        if (choice == null) {
            println("Пожалуйста, введите число")
        } else if (choice == 0) {
            return false
        } else {
            executeCommand(choice)
        }

        return true
    }

    private fun executeCommand(choice: Int) {
        when (choice) {
            1 -> playlistHandler.showAllSongs()
            2 -> userInputHandler.createPlaylistPrompt()
            3 -> userInputHandler.removePlaylistPrompt()
            4 -> playlistHandler.showAllPlaylists()
            5 -> userInputHandler.showPlaylistPrompt()
            6 -> userInputHandler.playPlaylistPrompt()
            7 -> userInputHandler.addSongToPlaylistPrompt()
            8 -> userInputHandler.removeSongFromPlaylistPrompt()
            9 -> playbackManager.playPreviousTrack()
            10 -> playbackManager.playNextTrack()
            11 -> playbackManager.replayCurrentTrack()
            12 -> playerService.pauseAndResumeTrack()
            else -> println("Неверный выбор\n")
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
}
