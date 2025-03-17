package service

import model.Playlist
import model.Track
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.nio.file.Path

class JsonServiceTest {
    private lateinit var jsonService: JsonService

    @BeforeEach
    fun setUp() {
        jsonService = JsonService()
    }

    @Test
    fun `savePlaylistToJson creates file with correct name`(
        @TempDir tempDir: Path,
    ) {
        val directory = tempDir.toFile()
        val playlistName = "testPlaylist"
        val playlist = Playlist(playlistName)

        jsonService.savePlaylistToJson(directory, playlist)

        val playerDir = File(directory, ".NotJavaPlayer")
        val jsonFile = File(playerDir, "$playlistName.json")
        assertTrue(jsonFile.exists())
        assertTrue(jsonFile.isFile)
    }

    @Test
    fun `savePlaylistToJson creates directory if it doesn't exist`(
        @TempDir tempDir: Path,
    ) {
        val directory = tempDir.toFile()
        val playlist = Playlist("testPlaylist")

        jsonService.savePlaylistToJson(directory, playlist)

        val playerDir = File(directory, ".NotJavaPlayer")
        assertTrue(playerDir.exists())
        assertTrue(playerDir.isDirectory)
    }

    @Test
    fun `savePlaylistToJson writes correct content to file`(
        @TempDir tempDir: Path,
    ) {
        val directory = tempDir.toFile()
        val playlistName = "testPlaylist"
        val track = Track("Test Track", "Test Artist", "path/to/file.mp3")
        val playlist = Playlist(playlistName, mutableListOf(track))

        jsonService.savePlaylistToJson(directory, playlist)

        val playerDir = File(directory, ".NotJavaPlayer")
        val jsonFile = File(playerDir, "$playlistName.json")
        val content = jsonFile.readText()
        assertTrue(content.contains(playlistName))
        assertTrue(content.contains("Test Track"))
        assertTrue(content.contains("Test Artist"))
    }

    @Test
    fun `loadPlaylistFromJson correctly loads playlist from file`(
        @TempDir tempDir: Path,
    ) {
        val directory = tempDir.toFile()
        val playlistName = "testPlaylist"
        val track = Track("path/to/file.mp3", "Test Track", "Test Artist")
        val originalPlaylist = Playlist(playlistName, mutableListOf(track))

        jsonService.savePlaylistToJson(directory, originalPlaylist)

        val playerDir = File(directory, ".NotJavaPlayer")
        val jsonFile = File(playerDir, "$playlistName.json")
        val loadedPlaylist = jsonService.loadPlaylistFromJson(jsonFile)

        assertEquals(playlistName, loadedPlaylist.name)
        assertEquals(1, loadedPlaylist.tracks.size)
        assertEquals("Test Track", loadedPlaylist.tracks[0].title)
        assertEquals("Test Artist", loadedPlaylist.tracks[0].artist)
    }

    @Test
    fun `loadPlaylistFromJson handles empty playlist`(
        @TempDir tempDir: Path,
    ) {
        val directory = tempDir.toFile()
        val playlistName = "emptyPlaylist"
        val originalPlaylist = Playlist(playlistName)

        jsonService.savePlaylistToJson(directory, originalPlaylist)

        val playerDir = File(directory, ".NotJavaPlayer")
        val jsonFile = File(playerDir, "$playlistName.json")
        val loadedPlaylist = jsonService.loadPlaylistFromJson(jsonFile)

        assertEquals(playlistName, loadedPlaylist.name)
        assertTrue(loadedPlaylist.tracks.isEmpty())
    }
}
