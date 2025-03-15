import repository.PlaylistRepository
import service.DirectoryService
import service.JsonService
import service.MetadataService
import service.PlayerService
import service.PlaylistService
import ui.ConsoleUI

fun main() {
    // Создание сервисов
    val directoryService = DirectoryService()
    val jsonService = JsonService()
    val metadataService = MetadataService()
    val playerService = PlayerService()

    // Создание репозитория
    val playlistRepository = PlaylistRepository(directoryService, jsonService, metadataService)

    // Создание сервиса плейлистов
    val playlistService = PlaylistService(playlistRepository)

    // Создание и запуск UI
    val ui = ConsoleUI(directoryService, playlistService, playerService)
    ui.start()
}
