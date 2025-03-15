package ui

import service.FileService
import service.PlayerListener
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
    private lateinit var playerListener: PlayerListener

    companion object {
        const val EXIT = 0
        const val SHOW_ALL_SONGS = 1
        const val CREATE_PLAYLIST = 2
        const val REMOVE_PLAYLIST = 3
        const val SHOW_ALL_PLAYLISTS = 4
        const val SHOW_PLAYLIST = 5
        const val PLAY_PLAYLIST = 6
        const val ADD_SONG_TO_PLAYLIST = 7
        const val REMOVE_SONG_FROM_PLAYLIST = 8
        const val PLAY_PREVIOUS_TRACK = 9
        const val PLAY_NEXT_TRACK = 10
        const val REPLAY_CURRENT_TRACK = 11
        const val PAUSE_RESUME_TRACK = 12
    }

    fun start() {
        musicDirectory = requestMusicDirectory()
        fileService.createPlayerFolder(musicDirectory)

        playerListener = PlayerListener { playbackManager.playNextTrack() }
        playbackManager = PlaybackManager(playerService, playerListener)
        playlistHandler = PlaylistHandler(playlistService, musicDirectory)
        userInputHandler = UserInputHandler(playlistHandler, playbackManager)

        val allSongs = playlistService.getAllSongs(musicDirectory)
        playlistService.savePlaylist(musicDirectory, allSongs)

        mainLoop()
        playerService.stopCurrentTrack()
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
        } else if (choice == EXIT) {
            return false
        } else {
            executeCommand(choice)
        }

        return true
    }

    private fun executeCommand(choice: Int) {
        when (choice) {
            SHOW_ALL_SONGS -> playlistHandler.showAllSongs()
            CREATE_PLAYLIST -> userInputHandler.createPlaylistPrompt()
            REMOVE_PLAYLIST -> userInputHandler.removePlaylistPrompt()
            SHOW_ALL_PLAYLISTS -> playlistHandler.showAllPlaylists()
            SHOW_PLAYLIST -> userInputHandler.showPlaylistPrompt()
            PLAY_PLAYLIST -> userInputHandler.playPlaylistPrompt()
            ADD_SONG_TO_PLAYLIST -> userInputHandler.addSongToPlaylistPrompt()
            REMOVE_SONG_FROM_PLAYLIST -> userInputHandler.removeSongFromPlaylistPrompt()
            PLAY_PREVIOUS_TRACK -> playbackManager.playPreviousTrack()
            PLAY_NEXT_TRACK -> playbackManager.playNextTrack()
            REPLAY_CURRENT_TRACK -> playbackManager.replayCurrentTrack()
            PAUSE_RESUME_TRACK -> playerService.pauseAndResumeTrack()
            else -> println("Неверный выбор\n")
        }
    }

    private fun displayMenu() {
        println("\nВыберите действие:")
        println("$SHOW_ALL_SONGS) Показать список песен")
        println("$CREATE_PLAYLIST) Создать плейлист")
        println("$REMOVE_PLAYLIST) Удалить плейлист")
        println("$SHOW_ALL_PLAYLISTS) Показать все плейлисты")
        println("$SHOW_PLAYLIST) Вывести плейлист")
        println("$PLAY_PLAYLIST) Включить плейлист")
        println("$ADD_SONG_TO_PLAYLIST) Добавить песню в плейлист")
        println("$REMOVE_SONG_FROM_PLAYLIST) Убрать песню из плейлиста")
        println("$PLAY_PREVIOUS_TRACK) Включить предыдущую песню")
        println("$PLAY_NEXT_TRACK) Включить следующую песню")
        println("$REPLAY_CURRENT_TRACK) Повторить текущую песню")
        println("$PAUSE_RESUME_TRACK) Поставить на паузу/возобновить воспроизведение")
        println("$EXIT) Выйти\n")
    }
}
