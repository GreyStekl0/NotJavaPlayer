package repository

import io.mockk.every
import io.mockk.mockk
import model.Playlist
import model.Track
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import service.IDirectoryService
import service.IJsonService
import service.IMetadataService
import java.io.File
import java.nio.file.Path

class PlaylistRepositoryTest {
    private lateinit var directoryService: IDirectoryService
    private lateinit var jsonService: IJsonService
    private lateinit var metadataService: IMetadataService
    private lateinit var playlistRepository: PlaylistRepository

    @BeforeEach
    fun setUp() {
        directoryService = mockk<IDirectoryService>()
        jsonService = mockk<IJsonService>()
        metadataService = mockk<IMetadataService>()
        playlistRepository = PlaylistRepository(directoryService, jsonService, metadataService)
    }

    @Test
    fun `getAllSongs returns all songs from directory`(
        @TempDir tempDir: Path,
    ) {
        val directory = tempDir.toFile()
        val file1 = File(directory, "file1.mp3")
        val file2 = File(directory, "file2.mp3")

        every { directoryService.findAllMp3Files(directory) } returns listOf(file1, file2)
        every { metadataService.createTrack(file1) } returns Track(file1.path, "file1", "Unknown")
        every { metadataService.createTrack(file2) } returns Track(file2.path, "file2", "Unknown")

        val result = playlistRepository.getAllSongs(directory)

        val expected = Playlist("All songs")
        expected.tracks.add(Track(file1.path, "file1", "Unknown"))
        expected.tracks.add(Track(file2.path, "file2", "Unknown"))
        assertEquals(expected, result)
    }

    @Test
    fun `getAllSongs returns empty playlist for empty directory`(
        @TempDir tempDir: Path,
    ) {
        val directory = tempDir.toFile()

        every { directoryService.findAllMp3Files(directory) } returns emptyList()

        val result = playlistRepository.getAllSongs(directory)

        val expected = Playlist("All songs")
        assertEquals(expected, result)
    }

    @Test
    fun `getAllPlaylists returns all playlists from directory`(
        @TempDir tempDir: Path,
    ) {
        val directory = tempDir.toFile()
        val file1 = File(directory, "file1.json")
        file1.createNewFile()
        val file2 = File(directory, "file2.json")
        file2.createNewFile()

        every { directoryService.createPlayerFolder(directory) } returns directory

        val result = playlistRepository.getAllPlaylists(directory)
        assertEquals(listOf("file2", "file1"), result)
    }

    @Test
    fun `getAllPlaylists returns empty list for empty directory`(
        @TempDir tempDir: Path,
    ) {
        val directory = tempDir.toFile()

        every { directoryService.createPlayerFolder(directory) } returns directory

        val result = playlistRepository.getAllPlaylists(directory)
        assertEquals(emptyList<String>(), result)
    }

    @Test
    fun `getPlaylist returns playlist from directory`(
        @TempDir tempDir: Path,
    ) {
        val directory = tempDir.toFile()
        val playerDir = File(directory, "player")
        playerDir.mkdir()
        val playlistFile = File(playerDir, "playlist.json")
        playlistFile.createNewFile()
        val playlist = Playlist("playlist")

        every { directoryService.createPlayerFolder(directory) } returns playerDir
        every { jsonService.loadPlaylistFromJson(playlistFile) } returns playlist

        val result = playlistRepository.getPlaylist(directory, "playlist")
        assertEquals(playlist, result)
    }

    @Test
    fun `getPlaylist returns null for non-existing playlist`(
        @TempDir tempDir: Path,
    ) {
        val directory = tempDir.toFile()
        val playerDir = File(directory, "player")
        playerDir.mkdir()

        every { directoryService.createPlayerFolder(directory) } returns playerDir

        val result = playlistRepository.getPlaylist(directory, "playlist")
        assertEquals(null, result)
    }

    @Test
    fun `deletePlaylist deletes playlist from directory`(
        @TempDir tempDir: Path,
    ) {
        val directory = tempDir.toFile()
        val playerDir = File(directory, "player")
        playerDir.mkdir()
        val playlistFile = File(playerDir, "playlist.json")
        playlistFile.createNewFile()

        every { directoryService.createPlayerFolder(directory) } returns playerDir

        val result = playlistRepository.deletePlaylist(directory, "playlist")
        assertEquals(true, result)
    }

    @Test
    fun `deletePlaylist returns false for non-existing playlist`(
        @TempDir tempDir: Path,
    ) {
        val directory = tempDir.toFile()
        val playerDir = File(directory, "player")
        playerDir.mkdir()

        every { directoryService.createPlayerFolder(directory) } returns playerDir

        val result = playlistRepository.deletePlaylist(directory, "playlist")
        assertEquals(false, result)
    }
}
