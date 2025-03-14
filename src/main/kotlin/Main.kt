import repository.PlaylistRepository
import service.FileService
import service.MetadataService
import service.PlayerService
import service.PlaylistService
import ui.ConsoleUI

fun main() {
    // Создание сервисов
    val fileService = FileService()
    val metadataService = MetadataService()
    val playerService = PlayerService()

    // Создание репозитория
    val playlistRepository = PlaylistRepository(fileService, metadataService)

    // Создание сервиса плейлистов
    val playlistService = PlaylistService(playlistRepository)

    // Создание и запуск UI
    val ui = ConsoleUI(fileService, playlistService, playerService)
    ui.start()
}
