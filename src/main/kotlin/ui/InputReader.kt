package ui

import service.IDirectoryService
import java.io.File

class InputReader(
    private val reader: () -> String = ::readln,
    private val writer: (String) -> Unit = ::println,
) {
    fun requestMusicDirectory(directoryService: IDirectoryService): File {
        writer(
            """
            Здравствуй дорогой пользователь
            Для работы "NOTJavaPlayer" введите путь к папке с музыкой
            Или нажмите Enter для использования папки по умолчанию:
            """.trimIndent(),
        )

        while (true) {
            val directoryInput = reader()

            if (directoryInput.replace(" ", "") == "") {
                return directoryService.getDefaultDirectory()
            }

            val directory = File(directoryInput)
            if (directory.exists()) {
                return directory
            } else {
                writer("Папка не найдена, попробуй еще раз")
            }
        }
    }
}
