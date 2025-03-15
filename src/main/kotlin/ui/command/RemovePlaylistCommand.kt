package ui.command

import ui.UserInputHandler

class RemovePlaylistCommand(
    private val userInputHandler: UserInputHandler,
) : Command {
    override fun execute() {
        userInputHandler.removePlaylistPrompt()
    }
}
