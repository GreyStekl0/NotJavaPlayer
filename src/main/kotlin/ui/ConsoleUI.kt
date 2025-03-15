package ui

import service.IDirectoryService
import service.IPlayerService
import service.IPlaylistService

class ConsoleUI(
    directoryService: IDirectoryService,
    playlistService: IPlaylistService,
    private val playerService: IPlayerService,
) {
    private val inputReader = InputReader()
    private val menuDisplay = MenuDisplay()
    private val componentInitializer =
        ComponentInitializer(directoryService, playlistService, playerService, inputReader)

    fun start() {
        componentInitializer.initializeComponents()
        componentInitializer.initializeCommands()
        mainLoop()
        playerService.stopCurrentTrack()
    }

    private fun mainLoop() {
        var running = true
        while (running) {
            menuDisplay.displayMenu()
            running = processUserChoice()
        }
    }

    private fun processUserChoice(): Boolean {
        val choice = inputReader.readIntOrNull()
        var shouldContinue = true

        if (choice == null) {
            println("Пожалуйста, введите число")
        } else {
            val menuOption = MenuOption.fromId(choice)
            when (menuOption) {
                MenuOption.EXIT -> shouldContinue = false
                null -> println("Неверный выбор\n")
                else -> executeCommand(menuOption)
            }
        }

        return shouldContinue
    }

    private fun executeCommand(option: MenuOption) {
        componentInitializer.commands[option]?.execute()
    }
}
