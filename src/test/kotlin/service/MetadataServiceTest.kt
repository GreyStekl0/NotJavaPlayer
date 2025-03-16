package service

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import org.jaudiotagger.audio.AudioFile
import org.jaudiotagger.audio.AudioFileIO
import org.jaudiotagger.tag.FieldKey
import org.jaudiotagger.tag.Tag
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.nio.file.Path

class MetadataServiceTest {
    private lateinit var metadataService: MetadataService
    private lateinit var mockAudioFile: AudioFile
    private lateinit var mockTag: Tag

    @TempDir
    lateinit var tempDir: Path

    @BeforeEach
    fun setUp() {
        metadataService = MetadataService()
        mockAudioFile = mockk<AudioFile>()
        mockTag = mockk<Tag>()

        mockkStatic(AudioFileIO::class)
        every { AudioFileIO.read(any<File>()) } returns mockAudioFile
        every { mockAudioFile.tag } returns mockTag
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `getMetadata returns title and artist from tags when present`() {
        val testFile = File(tempDir.toFile(), "test-song.mp3")

        every { mockTag.getFirst(FieldKey.TITLE) } returns "Test Title"
        every { mockTag.getFirst(FieldKey.ARTIST) } returns "Test Artist"

        val result = metadataService.getMetadata(testFile)

        assertEquals(2, result.size)
        assertEquals("Test Title", result[0])
        assertEquals("Test Artist", result[1])
    }

    @Test
    fun `getMetadata uses filename when title is empty`() {
        val testFile = File(tempDir.toFile(), "Filename.mp3")

        every { mockTag.getFirst(FieldKey.TITLE) } returns ""
        every { mockTag.getFirst(FieldKey.ARTIST) } returns "Test Artist"

        val result = metadataService.getMetadata(testFile)

        assertEquals("Filename", result[0])
        assertEquals("Test Artist", result[1])
    }

    @Test
    fun `getMetadata uses 'Unknown' when artist is empty`() {
        val testFile = File(tempDir.toFile(), "test-song.mp3")

        every { mockTag.getFirst(FieldKey.TITLE) } returns "Test Title"
        every { mockTag.getFirst(FieldKey.ARTIST) } returns ""

        val result = metadataService.getMetadata(testFile)

        assertEquals("Test Title", result[0])
        assertEquals("Unknown", result[1])
    }

    @Test
    fun `createTrack builds track object with metadata`() {
        val testFile = File(tempDir.toFile(), "song.mp3")

        every { mockTag.getFirst(FieldKey.TITLE) } returns "Test Title"
        every { mockTag.getFirst(FieldKey.ARTIST) } returns "Test Artist"

        val track = metadataService.createTrack(testFile)

        assertEquals(testFile.path, track.path)
        assertEquals("Test Title", track.title)
        assertEquals("Test Artist", track.artist)
    }

    @Test
    fun `createTrack uses filename and Unknown artist when tags are empty`() {
        val testFile = File(tempDir.toFile(), "song.mp3")

        every { mockTag.getFirst(FieldKey.TITLE) } returns ""
        every { mockTag.getFirst(FieldKey.ARTIST) } returns ""

        val track = metadataService.createTrack(testFile)

        assertEquals(testFile.path, track.path)
        assertEquals("song", track.title)
        assertEquals("Unknown", track.artist)
    }
}
