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
}
