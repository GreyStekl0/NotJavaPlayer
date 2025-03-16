import config.LoggerConfig
import repository.IPlaylistRepository
import repository.PlaylistRepository
import service.DirectoryService
import service.IDirectoryService
import service.IJsonService
import service.IMetadataService
import service.IPlayerService
import service.IPlaylistService
import service.JsonService
import service.MetadataService
import service.PlayerService
import service.PlaylistService
import ui.ConsoleUI

fun main() {
    LoggerConfig.configure()
    // Создание сервисов
    val directoryService: IDirectoryService = DirectoryService()
    val jsonService: IJsonService = JsonService()
    val metadataService: IMetadataService = MetadataService()
    val playerService: IPlayerService = PlayerService()

    // Создание репозитория
    val playlistRepository: IPlaylistRepository = PlaylistRepository(directoryService, jsonService, metadataService)

    // Создание сервиса плейлистов
    val playlistService: IPlaylistService = PlaylistService(playlistRepository)

    // Создание и запуск UI
    val ui = ConsoleUI(directoryService, playlistService, playerService)
    ui.start()
}
