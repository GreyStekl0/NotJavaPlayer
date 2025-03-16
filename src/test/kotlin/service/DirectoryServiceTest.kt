package service

import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.nio.file.Path

class DirectoryServiceTest {
    private lateinit var directoryService: DirectoryService

    @BeforeEach
    fun setUp() {
        directoryService = DirectoryService()
    }

    @Test
    fun `createPlayerFolder creates folder if it doesn't exist`(
        @TempDir tempDir: Path,
    ) {
        val directory = tempDir.toFile()

        val result = directoryService.createPlayerFolder(directory)

        assertTrue(result.exists())
        assertTrue(result.isDirectory)
        assertEquals(".NotJavaPlayer", result.name)
    }

    @Test
    fun `createPlayerFolder returns existing folder`(
        @TempDir tempDir: Path,
    ) {
        val directory = tempDir.toFile()
        val playerFolder = File(directory, ".NotJavaPlayer")
        playerFolder.mkdir()

        val result = directoryService.createPlayerFolder(directory)

        assertTrue(result.exists())
        assertEquals(playerFolder, result)
    }

    @Test
    fun `findAllMp3Files returns empty list for empty directory`(
        @TempDir tempDir: Path,
    ) {
        val directory = tempDir.toFile()

        val result = directoryService.findAllMp3Files(directory)

        assertTrue(result.isEmpty())
    }

    @Test
    fun `findAllMp3Files finds mp3 files in directory`(
        @TempDir tempDir: Path,
    ) {
        val directory = tempDir.toFile()
        val mp3File1 = File(directory, "test1.mp3").apply { createNewFile() }
        val mp3File2 = File(directory, "test2.mp3").apply { createNewFile() }
        File(directory, "test.txt").apply { createNewFile() }

        val result = directoryService.findAllMp3Files(directory)

        assertEquals(2, result.size)
        assertTrue(result.contains(mp3File1))
        assertTrue(result.contains(mp3File2))
    }

    @Test
    fun `findAllMp3Files finds mp3 files in subdirectories`(
        @TempDir tempDir: Path,
    ) {
        val directory = tempDir.toFile()
        val subDir = File(directory, "subdir").apply { mkdir() }

        val mp3File1 = File(directory, "test1.mp3").apply { createNewFile() }
        val mp3File2 = File(subDir, "test2.mp3").apply { createNewFile() }

        val result = directoryService.findAllMp3Files(directory)

        assertEquals(2, result.size)
        assertTrue(result.contains(mp3File1))
        assertTrue(result.contains(mp3File2))
    }

    @Test
    fun `findAllMp3Files skips hidden directories`(
        @TempDir tempDir: Path,
    ) {
        val directory = tempDir.toFile()
        val hiddenDir = File(directory, ".hidden").apply { mkdir() }

        val mp3File1 = File(directory, "test1.mp3").apply { createNewFile() }
        File(hiddenDir, "test2.mp3").apply { createNewFile() }

        val result = directoryService.findAllMp3Files(directory)

        assertEquals(1, result.size)
        assertTrue(result.contains(mp3File1))
    }

    @Test
    fun `getDefaultDirectory returns user home directory`() {
        val expectedDir = File(System.getProperty("user.home"))

        val result = directoryService.getDefaultDirectory()

        assertEquals(expectedDir, result)
    }
}
