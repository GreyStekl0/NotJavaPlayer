package ui

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import service.IDirectoryService
import java.io.File
import java.nio.file.Path

class InputReaderTest {
    @Test
    fun `requestMusicDirectory returns default directory on empty input`() {
        val directoryService = mockk<IDirectoryService>()
        val defaultDir = File("/default/dir")
        every { directoryService.getDefaultDirectory() } returns defaultDir

        val inputs = mutableListOf("")
        val outputs = mutableListOf<String>()

        val inputReader =
            InputReader(
                reader = { inputs.removeFirst() },
                writer = { outputs.add(it) },
            )

        val result = inputReader.requestMusicDirectory(directoryService)

        assertEquals(defaultDir, result)
    }

    @Test
    fun `requestMusicDirectory returns directory when valid path is entered`(
        @TempDir tempDir: Path,
    ) {
        val directoryService = mockk<IDirectoryService>()
        val validDir = tempDir.toFile()

        val inputs = mutableListOf(validDir.absolutePath)
        val outputs = mutableListOf<String>()

        val inputReader =
            InputReader(
                reader = { inputs.removeFirst() },
                writer = { outputs.add(it) },
            )

        val result = inputReader.requestMusicDirectory(directoryService)

        assertEquals(validDir, result)
    }

    @Test
    fun `requestMusicDirectory prompts again on invalid path`(
        @TempDir tempDir: Path,
    ) {
        val directoryService = mockk<IDirectoryService>()
        val invalidPath = "/this/path/does/not/exist"
        val validDir = tempDir.toFile()

        val inputs = mutableListOf(invalidPath, validDir.absolutePath)
        val outputs = mutableListOf<String>()

        val inputReader =
            InputReader(
                reader = { inputs.removeFirst() },
                writer = { outputs.add(it) },
            )

        val result = inputReader.requestMusicDirectory(directoryService)

        assertEquals(validDir, result)
        assertTrue(outputs.any { it == "Папка не найдена, попробуй еще раз" })
    }
}
