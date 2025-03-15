package ui

import service.IDirectoryService
import service.IPlayerService
import service.IPlaylistService
import service.PlayerListener
import ui.command.AddSongToPlaylistCommand
import ui.command.Command
import ui.command.CreatePlaylistCommand
import ui.command.PauseResumeTrackCommand
import ui.command.PlayNextTrackCommand
import ui.command.PlayPlaylistCommand
import ui.command.PlayPreviousTrackCommand
import ui.command.RemovePlaylistCommand
import ui.command.RemoveSongFromPlaylistCommand
import ui.command.ReplayCurrentTrackCommand
import ui.command.ShowAllPlaylistsCommand
import ui.command.ShowAllSongsCommand
import ui.command.ShowPlaylistCommand
import java.io.File

class ConsoleUI(
    private val directoryService: IDirectoryService,
    private val playlistService: IPlaylistService,
    private val playerService: IPlayerService,
) {
    private lateinit var commands: Map<MenuOption, Command>
    private lateinit var musicDirectory: File
    private lateinit var playbackManager: PlaybackManager
    private lateinit var playlistDisplay: PlaylistDisplay
    private lateinit var playlistManager: PlaylistManager
    private lateinit var userInputHandler: UserInputHandler
    private lateinit var playerListener: PlayerListener

    fun start() {
        initializeComponents()
        initializeCommands()
        mainLoop()
        playerService.stopCurrentTrack()
    }

    private fun initializeComponents() {
        musicDirectory = requestMusicDirectory()
        directoryService.createPlayerFolder(musicDirectory)

        playerListener = PlayerListener { playbackManager.playNextTrack() }
        playbackManager = PlaybackManager(playerService, playerListener)
        playlistManager = PlaylistManager(playlistService, musicDirectory)
        playlistDisplay = PlaylistDisplay(playlistService, musicDirectory)
        userInputHandler = UserInputHandler(playlistManager, playlistDisplay, playbackManager)

        val allSongs = playlistService.getAllSongs(musicDirectory)
        playlistService.savePlaylist(musicDirectory, allSongs)
    }

    private fun initializeCommands() {
        commands =
            mapOf(
                MenuOption.SHOW_ALL_SONGS to ShowAllSongsCommand(playlistDisplay),
                MenuOption.CREATE_PLAYLIST to CreatePlaylistCommand(userInputHandler),
                MenuOption.REMOVE_PLAYLIST to RemovePlaylistCommand(userInputHandler),
                MenuOption.SHOW_ALL_PLAYLISTS to ShowAllPlaylistsCommand(playlistDisplay),
                MenuOption.SHOW_PLAYLIST to ShowPlaylistCommand(userInputHandler),
                MenuOption.PLAY_PLAYLIST to PlayPlaylistCommand(userInputHandler),
                MenuOption.ADD_SONG_TO_PLAYLIST to AddSongToPlaylistCommand(userInputHandler),
                MenuOption.REMOVE_SONG_FROM_PLAYLIST to RemoveSongFromPlaylistCommand(userInputHandler),
                MenuOption.PLAY_PREVIOUS_TRACK to PlayPreviousTrackCommand(playbackManager),
                MenuOption.PLAY_NEXT_TRACK to PlayNextTrackCommand(playbackManager),
                MenuOption.REPLAY_CURRENT_TRACK to ReplayCurrentTrackCommand(playbackManager),
                MenuOption.PAUSE_RESUME_TRACK to PauseResumeTrackCommand(playerService),
            )
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
            directoryService.getDefaultDirectory()
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
        var shouldContinue = true

        if (choice == null) {
            println("Пожалуйста, введите число")
        } else {
            val menuOption = MenuOption.fromId(choice)
            when (menuOption) {
                MenuOption.EXIT -> shouldContinue = false
                null -> println("Неверный выбор\n")
                else -> executeCommand(menuOption)
            }
        }

        return shouldContinue
    }

    private fun executeCommand(option: MenuOption) {
        commands[option]?.execute()
    }

    private fun displayMenu() {
        println("\nВыберите действие:")
        MenuOption.entries.forEach { option ->
            if (option != MenuOption.EXIT) {
                println("${option.id}) ${option.description}")
            }
        }
        println("${MenuOption.EXIT.id}) ${MenuOption.EXIT.description}\n")
    }
}
