package ui

import service.IDirectoryService
import java.io.File

class InputReader {
    fun readLine(): String = readln()

    fun readIntOrNull(): Int? = readLine().toIntOrNull()

    fun requestMusicDirectory(directoryService: IDirectoryService): File {
        println(
            """
            Здравствуй дорогой пользователь
            Для работы "NOTJavaPlayer" введите путь к папке с музыкой
            Или нажмите Enter для использования папки по умолчанию:
            """.trimIndent(),
        )
        val directoryInput = readLine()
        return if (directoryInput.replace(" ", "") == "") {
            directoryService.getDefaultDirectory()
        } else {
            File(directoryInput)
        }
    }
}
