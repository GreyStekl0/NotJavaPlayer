package ui

import io.mockk.mockk
import model.Playlist
import model.Track
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import service.IPlaylistService
import service.PlaylistService
import java.io.File
import java.nio.file.Path

class PlaylistManagerTest {
    lateinit var playlistService: IPlaylistService
    lateinit var musicDirectory: File
    lateinit var playlistManager: PlaylistManager

    @BeforeEach
    fun setUp(
        @TempDir tempDir: Path,
    ) {
        playlistService = mockk<PlaylistService>()
        musicDirectory = tempDir.toFile()
        playlistManager = PlaylistManager(playlistService, musicDirectory)
    }

    @Test
    fun `getTrack returns track by index`() {
        val playlist = Playlist("testPlaylist", mutableListOf(Track("Test Track", "Test Artist", "path/to/file.mp3")))
        val track = playlistManager.getTrack(playlist, 1, "")
        assertEquals(playlist.tracks[0], track)
    }

    @Test
    fun `getTrack returns track by title`() {
        val playlist = Playlist("testPlaylist", mutableListOf(Track("path/to/file.mp3", "Test Track", "Test Artist")))
        val track = playlistManager.getTrack(playlist, null, "Test Track")
        assertEquals(playlist.tracks[0], track)
    }
}
