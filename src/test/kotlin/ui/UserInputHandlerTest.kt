package ui

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import model.Playlist
import model.Track
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test

class UserInputHandlerTest {
    lateinit var userInputHandler: UserInputHandler
    lateinit var playlistManager: PlaylistManager
    lateinit var playlistDisplay: PlaylistDisplay
    lateinit var playbackManager: PlaybackManager

    @BeforeEach
    fun setUp() {
        playlistManager = mockk<PlaylistManager>()
        playlistDisplay = mockk<PlaylistDisplay>()
        playbackManager = mockk<PlaybackManager>()
    }

    @Test
    fun `createPlaylistPrompt does not create playlist if it already exists`() {
        val outputs = mutableListOf<String>()

        userInputHandler =
            UserInputHandler(
                playlistManager,
                playlistDisplay,
                playbackManager,
                reader = { "existingPlaylist" },
                writer = { outputs.add(it) },
            )

        every { playlistManager.getPlaylist("existingPlaylist") } returns Playlist("existingPlaylist")

        userInputHandler.createPlaylistPrompt()

        assertTrue(outputs.any { it == "Плейлист 'existingPlaylist' уже существует" })
    }

    @Test
    fun `createPlaylistPrompt creates playlist`() {
        val outputs = mutableListOf<String>()

        userInputHandler =
            UserInputHandler(
                playlistManager,
                playlistDisplay,
                playbackManager,
                reader = { "newPlaylist" },
                writer = { outputs.add(it) },
            )

        every { playlistManager.getPlaylist("newPlaylist") } returns null
        every { playlistManager.createPlaylist("newPlaylist") } returns Unit

        userInputHandler.createPlaylistPrompt()

        assertTrue(outputs.any { it == "Плейлист 'newPlaylist' создан" })
    }

    @Test
    fun `removePlaylistPrompt does not remove playlist All songs`() {
        val outputs = mutableListOf<String>()

        userInputHandler =
            UserInputHandler(
                playlistManager,
                playlistDisplay,
                playbackManager,
                reader = { "All songs" },
                writer = { outputs.add(it) },
            )

        userInputHandler.removePlaylistPrompt()

        assertTrue(outputs.any { it == "Плейлист 'All songs' не может быть удален" })
    }

    @Test
    fun `removePlaylistPrompt removes playlist`() {
        val outputs = mutableListOf<String>()

        userInputHandler =
            UserInputHandler(
                playlistManager,
                playlistDisplay,
                playbackManager,
                reader = { "existingPlaylist" },
                writer = { outputs.add(it) },
            )

        every { playlistManager.removePlaylist("existingPlaylist") } returns true

        userInputHandler.removePlaylistPrompt()

        assertTrue(outputs.any { it == "Плейлист 'existingPlaylist' удален" })
    }

    @Test
    fun `removePlaylistPrompt does not remove playlist if it does not exist`() {
        val outputs = mutableListOf<String>()

        userInputHandler =
            UserInputHandler(
                playlistManager,
                playlistDisplay,
                playbackManager,
                reader = { "nonexistentPlaylist" },
                writer = { outputs.add(it) },
            )

        every { playlistManager.removePlaylist("nonexistentPlaylist") } returns false

        userInputHandler.removePlaylistPrompt()

        assertTrue(outputs.any { it == "Плейлист 'nonexistentPlaylist' не найден" })
    }

    @Test
    fun `playPlaylistPrompt does nothing when playlist not found`() {
        val inputs = mutableListOf("nonExistentPlaylist")
        val outputs = mutableListOf<String>()

        userInputHandler =
            UserInputHandler(
                playlistManager,
                playlistDisplay,
                playbackManager,
                { inputs.removeFirst() },
                { outputs.add(it) },
            )

        every { playlistDisplay.showAllPlaylists() } returns Unit
        every { playlistManager.getPlaylist("nonExistentPlaylist") } returns null

        userInputHandler.playPlaylistPrompt()

        assertTrue(outputs.any { it == "Плейлист 'nonExistentPlaylist' не найден" })
    }

    @Test
    fun `playPlaylistPrompt shows error when playlist is empty`() {
        val inputs = mutableListOf("emptyPlaylist")
        val outputs = mutableListOf<String>()
        val emptyPlaylist = Playlist("emptyPlaylist")

        userInputHandler =
            UserInputHandler(
                playlistManager,
                playlistDisplay,
                playbackManager,
                { inputs.removeFirst() },
                { outputs.add(it) },
            )

        every { playlistDisplay.showAllPlaylists() } returns Unit
        every { playlistManager.getPlaylist("emptyPlaylist") } returns emptyPlaylist

        userInputHandler.playPlaylistPrompt()

        assertTrue(outputs.any { it == "Плейлист пуст" })
    }

    @Test
    fun `playPlaylistPrompt plays tracks when playlist has songs`() {
        val inputs = mutableListOf("musicPlaylist")
        val outputs = mutableListOf<String>()
        val playlist =
            Playlist(
                "musicPlaylist",
                mutableListOf(Track("Song1", "Artist1", "path1")),
            )

        userInputHandler =
            UserInputHandler(
                playlistManager,
                playlistDisplay,
                playbackManager,
                { inputs.removeFirst() },
                { outputs.add(it) },
            )

        every { playlistDisplay.showAllPlaylists() } returns Unit
        every { playlistManager.getPlaylist("musicPlaylist") } returns playlist
        every { playbackManager.playPlaylist(playlist.tracks) } returns Unit

        userInputHandler.playPlaylistPrompt()

        verify { playbackManager.playPlaylist(playlist.tracks) }
    }

    @Test
    fun `addSongToPlaylistPrompt does nothing when selected playlist not found`() {
        val inputs = mutableListOf("nonExistentPlaylist")
        val outputs = mutableListOf<String>()

        userInputHandler =
            UserInputHandler(
                playlistManager,
                playlistDisplay,
                playbackManager,
                { inputs.removeFirst() },
                { outputs.add(it) },
            )

        every { playlistDisplay.showAllPlaylists() } returns Unit
        every { playlistManager.getPlaylist("nonExistentPlaylist") } returns null
        every { playlistManager.getPlaylist("All songs") } returns null

        userInputHandler.addSongToPlaylistPrompt()

        assertTrue(outputs.any { it == "Плейлист 'nonExistentPlaylist' не найден" })
    }

    @Test
    fun `addSongToPlaylistPrompt does nothing when All songs playlist not found`() {
        val inputs = mutableListOf("targetPlaylist")
        val outputs = mutableListOf<String>()
        val targetPlaylist = Playlist("targetPlaylist")

        userInputHandler =
            UserInputHandler(
                playlistManager,
                playlistDisplay,
                playbackManager,
                { inputs.removeFirst() },
                { outputs.add(it) },
            )

        every { playlistDisplay.showAllPlaylists() } returns Unit
        every { playlistManager.getPlaylist("targetPlaylist") } returns targetPlaylist
        every { playlistManager.getPlaylist("All songs") } returns null

        userInputHandler.addSongToPlaylistPrompt()

        verify { playlistManager.getPlaylist("All songs") }
    }

    @Test
    fun `addSongToPlaylistPrompt does nothing when track not found`() {
        val inputs = mutableListOf("targetPlaylist", "nonExistentTrack")
        val outputs = mutableListOf<String>()
        val targetPlaylist = Playlist("targetPlaylist")
        val allSongsPlaylist = Playlist("All songs")

        userInputHandler =
            UserInputHandler(
                playlistManager,
                playlistDisplay,
                playbackManager,
                { inputs.removeFirst() },
                { outputs.add(it) },
            )

        every { playlistDisplay.showAllPlaylists() } returns Unit
        every { playlistManager.getPlaylist("targetPlaylist") } returns targetPlaylist
        every { playlistManager.getPlaylist("All songs") } returns allSongsPlaylist
        every { playlistDisplay.showAllSongs() } returns Unit
        every { playlistManager.getTrack(allSongsPlaylist, null, "nonExistentTrack") } returns null

        userInputHandler.addSongToPlaylistPrompt()

        assertTrue(outputs.any { it == "Песня не найдена" })
    }

    @Test
    fun `addSongToPlaylistPrompt successfully adds track to playlist`() {
        val inputs = mutableListOf("targetPlaylist", "trackName")
        val outputs = mutableListOf<String>()
        val targetPlaylist = Playlist("targetPlaylist")
        val allSongsPlaylist = Playlist("All songs")
        val track = Track("trackName", "Artist", "path")

        userInputHandler =
            UserInputHandler(
                playlistManager,
                playlistDisplay,
                playbackManager,
                { inputs.removeFirst() },
                { outputs.add(it) },
            )

        every { playlistDisplay.showAllPlaylists() } returns Unit
        every { playlistManager.getPlaylist("targetPlaylist") } returns targetPlaylist
        every { playlistManager.getPlaylist("All songs") } returns allSongsPlaylist
        every { playlistDisplay.showAllSongs() } returns Unit
        every { playlistManager.getTrack(allSongsPlaylist, null, "trackName") } returns track
        every { playlistManager.addTrackToPlaylist("targetPlaylist", track) } returns true

        userInputHandler.addSongToPlaylistPrompt()

        assertTrue(outputs.any { it == "Песня добавлена в плейлист 'targetPlaylist'" })
    }

    @Test
    fun `addSongToPlaylistPrompt shows message when track already in playlist`() {
        val inputs = mutableListOf("targetPlaylist", "trackName")
        val outputs = mutableListOf<String>()
        val targetPlaylist = Playlist("targetPlaylist")
        val allSongsPlaylist = Playlist("All songs")
        val track = Track("trackName", "Artist", "path")

        userInputHandler =
            UserInputHandler(
                playlistManager,
                playlistDisplay,
                playbackManager,
                { inputs.removeFirst() },
                { outputs.add(it) },
            )

        every { playlistDisplay.showAllPlaylists() } returns Unit
        every { playlistManager.getPlaylist("targetPlaylist") } returns targetPlaylist
        every { playlistManager.getPlaylist("All songs") } returns allSongsPlaylist
        every { playlistDisplay.showAllSongs() } returns Unit
        every { playlistManager.getTrack(allSongsPlaylist, null, "trackName") } returns track
        every { playlistManager.addTrackToPlaylist("targetPlaylist", track) } returns false

        userInputHandler.addSongToPlaylistPrompt()

        assertTrue(outputs.any { it == "Песня уже есть в плейлисте 'targetPlaylist'" })
    }

    @Test
    fun `removeSongFromPlaylistPrompt does nothing when selected playlist not found`() {
        val inputs = mutableListOf("nonExistentPlaylist")
        val outputs = mutableListOf<String>()

        userInputHandler =
            UserInputHandler(
                playlistManager,
                playlistDisplay,
                playbackManager,
                { inputs.removeFirst() },
                { outputs.add(it) },
            )

        every { playlistDisplay.showAllPlaylists() } returns Unit
        every { playlistManager.getPlaylist("nonExistentPlaylist") } returns null
        every { playlistManager.getPlaylist("All songs") } returns null

        userInputHandler.removeSongFromPlaylistPrompt()

        assertTrue(outputs.any { it == "Плейлист 'nonExistentPlaylist' не найден" })
    }

    @Test
    fun `removeSongFromPlaylistPrompt does nothing when playlist empty`() {
        val inputs = mutableListOf("epmtyPlaylist")
        val outputs = mutableListOf<String>()
        val emptyPlaylist = Playlist("epmtyPlaylist")

        userInputHandler =
            UserInputHandler(
                playlistManager,
                playlistDisplay,
                playbackManager,
                { inputs.removeFirst() },
                { outputs.add(it) },
            )

        every { playlistDisplay.showAllPlaylists() } returns Unit
        every { playlistManager.getPlaylist("epmtyPlaylist") } returns emptyPlaylist

        userInputHandler.removeSongFromPlaylistPrompt()

        assertTrue(outputs.any { it == "Плейлист пуст" })
    }

    @Test
    fun `removeSongFromPlaylistPrompt remove song from playlist`() {
        val inputs = mutableListOf("playlistName", "trackName")
        val outputs = mutableListOf<String>()
        val playlist = Playlist("playlistName", mutableListOf(Track("path", "trackName", "Artist")))

        userInputHandler =
            UserInputHandler(
                playlistManager,
                playlistDisplay,
                playbackManager,
                { inputs.removeFirst() },
                { outputs.add(it) },
            )

        every { playlistDisplay.showAllPlaylists() } returns Unit
        every { playlistManager.getPlaylist("playlistName") } returns playlist
        every { playlistDisplay.showPlaylist("playlistName") } returns Unit
        every { playlistManager.getTrack(playlist, null, "trackName") } returns playlist.tracks[0]
        every { playlistManager.removeTrackFromPlaylist("playlistName", playlist.tracks[0]) } returns true

        userInputHandler.removeSongFromPlaylistPrompt()

        assertTrue(outputs.any { it == "Песня удалена из плейлиста 'playlistName'" })
    }
}
