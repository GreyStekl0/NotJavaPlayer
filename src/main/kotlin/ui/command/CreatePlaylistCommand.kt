package ui.command

import ui.UserInputHandler

class CreatePlaylistCommand(
    private val userInputHandler: UserInputHandler,
) : Command {
    override fun execute() {
        userInputHandler.createPlaylistPrompt()
    }
}
