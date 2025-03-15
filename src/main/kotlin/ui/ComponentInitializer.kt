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

class
ComponentInitializer(
    private val directoryService: IDirectoryService,
    private val playlistService: IPlaylistService,
    private val playerService: IPlayerService,
    private val inputReader: InputReader,
) {
    lateinit var musicDirectory: File
    lateinit var playbackManager: PlaybackManager
    lateinit var playlistDisplay: PlaylistDisplay
    lateinit var playlistManager: PlaylistManager
    lateinit var userInputHandler: UserInputHandler
    lateinit var playerListener: PlayerListener
    lateinit var commands: Map<MenuOption, Command>

    fun initializeComponents() {
        musicDirectory = inputReader.requestMusicDirectory(directoryService)
        directoryService.createPlayerFolder(musicDirectory)

        playerListener = PlayerListener { playbackManager.playNextTrack() }
        playbackManager = PlaybackManager(playerService, playerListener)
        playlistManager = PlaylistManager(playlistService, musicDirectory)
        playlistDisplay = PlaylistDisplay(playlistService, musicDirectory)

        userInputHandler = UserInputHandler(playlistManager, playlistDisplay, playbackManager)

        val allSongs = playlistService.getAllSongs(musicDirectory)
        playlistService.savePlaylist(musicDirectory, allSongs)
    }

    fun initializeCommands() {
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
}
